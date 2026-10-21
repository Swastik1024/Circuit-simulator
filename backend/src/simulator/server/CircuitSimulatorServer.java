package simulator.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import simulator.engine.CircuitBoard;
import simulator.model.CircuitNode;
import simulator.model.LedBulb;
import simulator.model.WireConnection;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Zero-Dependency Java HTTP Server (uses JDK standard com.sun.net.httpserver.HttpServer).
 * Exposes REST API endpoints (/api/simulate, /api/health) and serves the static Frontend!
 */
public class CircuitSimulatorServer {
    private final int port;
    private HttpServer server;

    public CircuitSimulatorServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/health", new HealthHandler());
        server.createContext("/api/simulate", new SimulateHandler());
        server.createContext("/", new StaticFrontendHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("==============================================================");
        System.out.println("  Java OOP Electrical & Logic Circuit Simulator Server Ready  ");
        System.out.println("  Frontend UI : http://localhost:" + port);
        System.out.println("  REST API    : http://localhost:" + port + "/api/simulate");
        System.out.println("==============================================================");
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    private static void addCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }

    private static void sendJson(HttpExchange exchange, int status, String json) throws IOException {
        addCorsHeaders(exchange);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    static class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                addCorsHeaders(exchange);
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            sendJson(exchange, 200, "{\"status\":\"UP\",\"engine\":\"Java OOP CircuitEngine v2.0\",\"javaVersion\":\"" + System.getProperty("java.version") + "\"}");
        }
    }

    /**
     * Parses incoming circuit JSON payload, builds the Java OOP object graph
     * (InputSource, LogicGate subclasses, LedBulb, WireConnection), runs simulate(),
     * and returns evaluated node states, glowing LED status, and Boolean expressions.
     */
    static class SimulateHandler implements HttpHandler {
        private static final Pattern NODE_PATTERN = Pattern.compile(
            "\{\s*\"id\"\s*:\s*\"([^\"]+)\"\s*,\s*\"type\"\s*:\s*\"([^\"]+)\"\s*,\s*\"name\"\s*:\s*\"([^\"]+)\"\s*,\s*\"x\"\s*:\s*([0-9.-]+)\s*,\s*\"y\"\s*:\s*([0-9.-]+)\s*,\s*\"state\"\s*:\s*(true|false)\s*,\s*\"inputs\"\s*:\s*(\d+)"
        );
        private static final Pattern WIRE_PATTERN = Pattern.compile(
            "\{\s*\"id\"\s*:\s*\"([^\"]+)\"\s*,\s*\"from\"\s*:\s*\"([^\"]+)\"\s*,\s*\"to\"\s*:\s*\"([^\"]+)\"\s*,\s*\"toPin\"\s*:\s*(\d+)"
        );

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                addCorsHeaders(exchange);
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJson(exchange, 405, "{\"error\":\"Method Not Allowed\"}");
                return;
            }

            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            CircuitBoard board = new CircuitBoard();

            Matcher nodeMatcher = NODE_PATTERN.matcher(body);
            while (nodeMatcher.find()) {
                String id = nodeMatcher.group(1);
                String type = nodeMatcher.group(2);
                String name = nodeMatcher.group(3);
                double x = Double.parseDouble(nodeMatcher.group(4));
                double y = Double.parseDouble(nodeMatcher.group(5));
                boolean state = Boolean.parseBoolean(nodeMatcher.group(6));
                int inputs = Integer.parseInt(nodeMatcher.group(7));

                if ("SOURCE".equalsIgnoreCase(type)) {
                    board.addInputSource(id, name, x, y, state);
                } else if ("LED".equalsIgnoreCase(type)) {
                    board.addLedBulb(id, name, x, y, "#10b981");
                } else {
                    board.addLogicGate(type, id, name, x, y, inputs);
                }
            }

            Matcher wireMatcher = WIRE_PATTERN.matcher(body);
            while (wireMatcher.find()) {
                String wId = wireMatcher.group(1);
                String from = wireMatcher.group(2);
                String to = wireMatcher.group(3);
                int toPin = Integer.parseInt(wireMatcher.group(4));
                board.connectWire(wId, from, to, toPin);
            }

            board.simulate();

            StringBuilder sb = new StringBuilder();
            sb.append("{\"engine\":\"Java OOP CircuitBoard\",\"nodes\":[");
            boolean first = true;
            for (CircuitNode n : board.getNodes().values()) {
                if (!first) sb.append(",");
                first = false;
                String expr = escapeJson(n.toBooleanExpression(new HashSet<>()));
                String diag = escapeJson(n.getDiagnosticMessage());
                boolean glowing = (n instanceof LedBulb) && ((LedBulb) n).isGlowing();
                sb.append(String.format(
                    "{\"id\":\"%s\",\"name\":\"%s\",\"type\":\"%s\",\"output\":%b,\"valid\":%b,\"glowing\":%b,\"expression\":\"%s\",\"diagnostic\":\"%s\"}",
                    escapeJson(n.getId()), escapeJson(n.getName()), escapeJson(n.getType()),
                    n.getCurrentOutputState(), n.isValidConnection(), glowing, expr, diag
                ));
            }
            sb.append("],\"wires\":[");
            first = true;
            for (WireConnection w : board.getWires()) {
                if (!first) sb.append(",");
                first = false;
                sb.append(String.format(
                    "{\"id\":\"%s\",\"carryingCurrent\":%b,\"validPath\":%b,\"voltage\":%.1f}",
                    escapeJson(w.getId()), w.isCarryingCurrent(), w.isValidPath(), w.getVoltage()
                ));
            }
            sb.append("]}");

            sendJson(exchange, 200, sb.toString());
        }

        private static String escapeJson(String s) {
            if (s == null) return "";
            return s.replace("\\", "\\\\").replace("\"", "\\\"");
        }
    }

    static class StaticFrontendHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            File file = new File("frontend/index.html");
            if (!file.exists()) {
                file = new File("../frontend/index.html");
            }
            if (file.exists()) {
                byte[] bytes = Files.readAllBytes(file.toPath());
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            } else {
                String msg = "Frontend index.html not found in current working directory. Open frontend/index.html directly in your browser.";
                exchange.sendResponseHeaders(404, msg.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(msg.getBytes(StandardCharsets.UTF_8));
                }
            }
        }
    }
}
