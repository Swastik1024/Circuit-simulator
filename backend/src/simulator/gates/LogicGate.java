package simulator.gates;

import simulator.model.CircuitNode;
import simulator.model.WireConnection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * OOP Concepts: Abstract Subclass, Template Method Pattern, Polymorphism
 * Base class for all digital logic gates (AND, OR, NOT, NAND, NOR, XOR, XNOR).
 * Handles recursive wire traversal, floating-pin validation, and cycle detection,
 * while delegating the specific truth-table math to subclasses via computeGateLogic().
 */
public abstract class LogicGate extends CircuitNode {
    private final String icNumber;
    private final String booleanSymbol;

    protected LogicGate(String id, String name, String gateType, double x, double y,
                        int inputPinCount, String icNumber, String booleanSymbol) {
        super(id, name, gateType, x, y, inputPinCount, 1);
        this.icNumber = icNumber;
        this.booleanSymbol = booleanSymbol;
    }

    public String getIcNumber() { return icNumber; }
    public String getBooleanSymbol() { return booleanSymbol; }

    /**
     * Polymorphic hook implemented by each concrete gate class (AND, OR, NOT, etc.).
     *
     * @param inputs List of boolean input signals (size == getInputPinCount())
     * @return Computed gate output
     */
    protected abstract boolean computeGateLogic(List<Boolean> inputs);

    @Override
    public boolean evaluate(Map<String, Boolean> memo, Set<String> visited) {
        if (memo.containsKey(getId())) {
            return memo.get(getId());
        }
        if (visited.contains(getId())) {
            setValidConnection(false);
            setCurrentOutputState(false);
            setDiagnosticMessage("Feedback loop / short-circuit detected involving gate " + getName());
            return false;
        }

        Set<String> nextVisited = new HashSet<>(visited);
        nextVisited.add(getId());

        // Check that all required input pins have a connected wire
        int requiredPins = getInputPinCount();
        List<WireConnection> wires = new ArrayList<>(getIncomingWires());
        wires.sort(Comparator.comparingInt(WireConnection::getTargetPinIndex));

        if (wires.size() < requiredPins) {
            setValidConnection(false);
            setCurrentOutputState(false);
            int missing = requiredPins - wires.size();
            setDiagnosticMessage(getType() + " Gate '" + getName() + "' has " + missing + " unconnected input pin(s).");
            memo.put(getId(), false);
            return false;
        }

        List<Boolean> inputSignals = new ArrayList<>(requiredPins);
        boolean allInputsValid = true;

        for (WireConnection wire : wires) {
            CircuitNode source = wire.getSourceNode();
            boolean val = source.evaluate(memo, nextVisited);
            boolean srcValid = source.isValidConnection();

            wire.setValidPath(srcValid);
            wire.setCarryingCurrent(srcValid && val);

            if (!srcValid) {
                allInputsValid = false;
            }
            inputSignals.add(val);
        }

        if (!allInputsValid) {
            setValidConnection(false);
            setCurrentOutputState(false);
            setDiagnosticMessage(getType() + " Gate '" + getName() + "' receives an incomplete signal from an upstream gate.");
            memo.put(getId(), false);
            return false;
        }

        boolean result = computeGateLogic(inputSignals);
        setValidConnection(true);
        setCurrentOutputState(result);
        setDiagnosticMessage(getType() + " Gate (" + icNumber + ") active: Output = " + (result ? "1 (+5V)" : "0 (0V)"));
        memo.put(getId(), result);
        return result;
    }

    @Override
    public String toBooleanExpression(Set<String> visited) {
        if (visited.contains(getId())) {
            return "LOOP";
        }
        Set<String> nextVisited = new HashSet<>(visited);
        nextVisited.add(getId());

        List<WireConnection> wires = new ArrayList<>(getIncomingWires());
        wires.sort(Comparator.comparingInt(WireConnection::getTargetPinIndex));

        if (wires.size() < getInputPinCount()) {
            return "?";
        }

        List<String> subExpressions = new ArrayList<>();
        for (WireConnection w : wires) {
            subExpressions.add(w.getSourceNode().toBooleanExpression(nextVisited));
        }
        return formatExpression(subExpressions);
    }

    protected abstract String formatExpression(List<String> inputExpressions);
}
