package simulator.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * OOP Concept: Inheritance & State Validation
 * Represents an Output LED Bulb indicator. Glows brightly ONLY when:
 * 1) Its input terminal is properly wired to a valid circuit path (no floating pins upstream), AND
 * 2) The upstream circuit evaluates to Logic HIGH (+5V).
 */
public class LedBulb extends CircuitNode {
    private String colorHex;
    private boolean glowing;

    public LedBulb(String id, String name, double x, double y, String colorHex) {
        super(id, name, "LED", x, y, 1, 0);
        this.colorHex = (colorHex == null || colorHex.isEmpty()) ? "#10b981" : colorHex;
        this.glowing = false;
    }

    public String getColorHex() { return colorHex; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }

    public boolean isGlowing() { return glowing; }

    @Override
    public boolean evaluate(Map<String, Boolean> memo, Set<String> visited) {
        if (getIncomingWires().isEmpty()) {
            this.glowing = false;
            setCurrentOutputState(false);
            setValidConnection(false);
            setDiagnosticMessage("Unconnected LED: Drag a wire from a gate output or source to this LED's input pin.");
            memo.put(getId(), false);
            return false;
        }

        WireConnection wire = getIncomingWires().get(0);
        CircuitNode driver = wire.getSourceNode();

        if (visited.contains(getId())) {
            this.glowing = false;
            setCurrentOutputState(false);
            setValidConnection(false);
            setDiagnosticMessage("Combinational feedback loop detected at LED " + getName());
            return false;
        }

        Set<String> nextVisited = new HashSet<>(visited);
        nextVisited.add(getId());

        boolean signal = driver.evaluate(memo, nextVisited);
        boolean driverValid = driver.isValidConnection();

        wire.setValidPath(driverValid);
        wire.setCarryingCurrent(driverValid && signal);

        setValidConnection(driverValid);
        setCurrentOutputState(driverValid && signal);
        this.glowing = driverValid && signal;

        if (!driverValid) {
            setDiagnosticMessage("Circuit Incomplete: Upstream component '" + driver.getName() + "' has unconnected pins.");
        } else if (this.glowing) {
            setDiagnosticMessage("LED GLOWING: Valid circuit connection delivering +5V (Logic 1 / ~20mA).");
        } else {
            setDiagnosticMessage("LED OFF (Valid Connection): Circuit is properly wired and outputs 0V (Logic 0).");
        }

        memo.put(getId(), this.glowing);
        return this.glowing;
    }

    @Override
    public String toBooleanExpression(Set<String> visited) {
        if (getIncomingWires().isEmpty()) {
            return "UNCONNECTED";
        }
        CircuitNode src = getIncomingWires().get(0).getSourceNode();
        if (visited.contains(getId())) {
            return "LOOP";
        }
        Set<String> next = new HashSet<>(visited);
        next.add(getId());
        return src.toBooleanExpression(next);
    }

    @Override
    public String getEducationalNote() {
        return "Output LED Indicator: Forward-biased light-emitting diode with series current-limiting resistor. Glows when input is +5V HIGH and all upstream gate inputs are properly connected.";
    }
}
