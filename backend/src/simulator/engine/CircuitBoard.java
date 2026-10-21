package simulator.engine;

import simulator.gates.GateFactory;
import simulator.gates.LogicGate;
import simulator.model.CircuitNode;
import simulator.model.InputSource;
import simulator.model.LedBulb;
import simulator.model.WireConnection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * OOP Concept: Composition, Aggregation & Facade Pattern
 * Manages the collection of CircuitNodes (InputSources, LogicGates, LedBulbs)
 * and WireConnections, executing full-circuit simulation, Boolean algebra synthesis,
 * and automatic Truth Table generation.
 */
public class CircuitBoard {
    private final Map<String, CircuitNode> nodes = new LinkedHashMap<>();
    private final List<WireConnection> wires = new ArrayList<>();

    public void clear() {
        nodes.clear();
        wires.clear();
    }

    public void addNode(CircuitNode node) {
        nodes.put(node.getId(), node);
    }

    public InputSource addInputSource(String id, String name, double x, double y, boolean initialState) {
        InputSource src = new InputSource(id, name, x, y, initialState);
        addNode(src);
        return src;
    }

    public LogicGate addLogicGate(String gateType, String id, String name, double x, double y, int inputs) {
        LogicGate gate = GateFactory.createGate(gateType, id, name, x, y, inputs);
        addNode(gate);
        return gate;
    }

    public LedBulb addLedBulb(String id, String name, double x, double y, String colorHex) {
        LedBulb led = new LedBulb(id, name, x, y, colorHex);
        addNode(led);
        return led;
    }

    public WireConnection connectWire(String wireId, String fromNodeId, String toNodeId, int targetPinIndex) {
        CircuitNode source = nodes.get(fromNodeId);
        CircuitNode target = nodes.get(toNodeId);
        if (source == null || target == null) {
            throw new IllegalArgumentException("Cannot connect wire: missing source or target component.");
        }
        // Remove existing wire on the same target pin slot if any
        wires.removeIf(w -> w.getTargetNode().getId().equals(toNodeId) && w.getTargetPinIndex() == targetPinIndex);
        target.getIncomingWires().removeIf(w -> w.getTargetPinIndex() == targetPinIndex);

        WireConnection wire = new WireConnection(wireId, source, target, targetPinIndex);
        wires.add(wire);
        target.addIncomingWire(wire);
        return wire;
    }

    /**
     * Runs a full evaluation pass across all nodes and wires on the board.
     */
    public Map<String, Boolean> simulate() {
        Map<String, Boolean> memo = new HashMap<>();
        for (WireConnection w : wires) {
            w.setCarryingCurrent(false);
            w.setValidPath(true);
        }
        for (CircuitNode node : nodes.values()) {
            node.evaluate(memo, new HashSet<>());
        }
        return memo;
    }

    /**
     * Generates a complete truth table for up to 6 input sources and all LED outputs.
     */
    public List<Map<String, Boolean>> generateTruthTable() {
        List<InputSource> sources = getInputSources();
        List<LedBulb> leds = getLedBulbs();
        if (sources.isEmpty() || sources.size() > 6) {
            return Collections.emptyList();
        }

        // Save original states
        Map<String, Boolean> savedStates = new HashMap<>();
        for (InputSource s : sources) {
            savedStates.put(s.getId(), s.isActiveHigh());
        }

        int totalRows = 1 << sources.size();
        List<Map<String, Boolean>> table = new ArrayList<>(totalRows);

        for (int mask = 0; mask < totalRows; mask++) {
            Map<String, Boolean> row = new LinkedHashMap<>();
            for (int i = 0; i < sources.size(); i++) {
                boolean bit = ((mask >> (sources.size() - 1 - i)) & 1) == 1;
                InputSource src = sources.get(i);
                src.setActiveHigh(bit);
                row.put(src.getName(), bit);
            }
            simulate();
            for (LedBulb led : leds) {
                row.put(led.getName(), led.isGlowing());
            }
            table.add(row);
        }

        // Restore original states and re-simulate
        for (InputSource s : sources) {
            s.setActiveHigh(savedStates.get(s.getId()));
        }
        simulate();
        return table;
    }

    public List<InputSource> getInputSources() {
        List<InputSource> list = new ArrayList<>();
        for (CircuitNode n : nodes.values()) {
            if (n instanceof InputSource) list.add((InputSource) n);
        }
        return list;
    }

    public List<LedBulb> getLedBulbs() {
        List<LedBulb> list = new ArrayList<>();
        for (CircuitNode n : nodes.values()) {
            if (n instanceof LedBulb) list.add((LedBulb) n);
        }
        return list;
    }

    public Map<String, CircuitNode> getNodes() { return Collections.unmodifiableMap(nodes); }
    public List<WireConnection> getWires() { return Collections.unmodifiableList(wires); }
}
