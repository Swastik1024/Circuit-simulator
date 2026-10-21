package simulator;

import simulator.engine.CircuitBoard;
import simulator.gates.LogicGate;
import simulator.model.InputSource;
import simulator.model.LedBulb;
import simulator.server.CircuitSimulatorServer;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Main Entrypoint for the First-Year OOP Java Electrical & Logic Circuit Simulator.
 *
 * 1) Runs a built-in CLI demonstration of the OOP hierarchy (Half-Adder & All 7 Gates verification)
 * 2) Starts the embedded Java HTTP Backend Server on http://localhost:8080 (unless --demo-only is passed)
 */
public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("==============================================================");
        System.out.println("  OOP IN JAVA: DIGITAL & ELECTRICAL CIRCUIT SIMULATOR         ");
        System.out.println("==============================================================");

        runSelfTestAndDemo();

        boolean demoOnly = false;
        int port = 8080;
        for (String arg : args) {
            if ("--demo-only".equalsIgnoreCase(arg)) {
                demoOnly = true;
            } else if (arg.startsWith("--port=")) {
                port = Integer.parseInt(arg.substring("--port=".length()));
            }
        }

        if (!demoOnly) {
            CircuitSimulatorServer server = new CircuitSimulatorServer(port);
            server.start();
        }
    }

    private static void runSelfTestAndDemo() {
        CircuitBoard board = new CircuitBoard();

        // 1. Add user-named voltage/logic sources A and B
        InputSource srcA = board.addInputSource("src_A", "A", 80, 120, true);
        InputSource srcB = board.addInputSource("src_B", "B", 80, 260, true);

        // 2. Add XOR gate (Sum) and AND gate (Carry) to form a Half-Adder
        LogicGate xorGate = board.addLogicGate("XOR", "gate_xor", "SUM_XOR", 300, 120, 2);
        LogicGate andGate = board.addLogicGate("AND", "gate_and", "CARRY_AND", 300, 260, 2);

        // 3. Add Output LED Bulbs
        LedBulb ledSum = board.addLedBulb("led_sum", "LED_SUM", 540, 120, "#10b981");
        LedBulb ledCarry = board.addLedBulb("led_carry", "LED_CARRY", 540, 260, "#3b82f6");

        // 4. Connect Wires
        board.connectWire("w1", srcA.getId(), xorGate.getId(), 0);
        board.connectWire("w2", srcB.getId(), xorGate.getId(), 1);
        board.connectWire("w3", srcA.getId(), andGate.getId(), 0);
        board.connectWire("w4", srcB.getId(), andGate.getId(), 1);
        board.connectWire("w5", xorGate.getId(), ledSum.getId(), 0);
        board.connectWire("w6", andGate.getId(), ledCarry.getId(), 0);

        // 5. Run simulation
        board.simulate();

        System.out.println("[Self-Test] Half-Adder Circuit Evaluation (A=1, B=1):");
        System.out.println("  -> " + ledSum.getName() + " Expression   : " + ledSum.toBooleanExpression(new HashSet<>()));
        System.out.println("  -> " + ledSum.getName() + " Glowing      : " + ledSum.isGlowing() + " (" + ledSum.getDiagnosticMessage() + ")");
        System.out.println("  -> " + ledCarry.getName() + " Expression : " + ledCarry.toBooleanExpression(new HashSet<>()));
        System.out.println("  -> " + ledCarry.getName() + " Glowing    : " + ledCarry.isGlowing() + " (" + ledCarry.getDiagnosticMessage() + ")");

        System.out.println("\n[Self-Test] Generated Truth Table:");
        List<Map<String, Boolean>> table = board.generateTruthTable();
        for (Map<String, Boolean> row : table) {
            System.out.println("  " + row);
        }
        System.out.println();
    }
}
