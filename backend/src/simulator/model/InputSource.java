package simulator.model;

import java.util.Map;
import java.util.Set;

/**
 * OOP Concept: Inheritance & Specialization
 * Represents a user-named DC Voltage / Digital Logic Source (0V LOW or +5V HIGH).
 * Students can add unlimited sources, rename them (e.g., A, B, Cin, V_in), and toggle their states.
 */
public class InputSource extends CircuitNode {
    private boolean activeHigh;
    private double voltageVolts;

    public InputSource(String id, String customName, double x, double y, boolean initialState) {
        super(id, customName, "SOURCE", x, y, 0, 1);
        this.activeHigh = initialState;
        this.voltageVolts = initialState ? 5.0 : 0.0;
        setCurrentOutputState(initialState);
        setValidConnection(true);
        setDiagnosticMessage(initialState ? "Source Active (+5V HIGH / Logic 1)" : "Source Grounded (0V LOW / Logic 0)");
    }

    public boolean isActiveHigh() { return activeHigh; }

    public void setActiveHigh(boolean activeHigh) {
        this.activeHigh = activeHigh;
        this.voltageVolts = activeHigh ? 5.0 : 0.0;
        setCurrentOutputState(activeHigh);
        setDiagnosticMessage(activeHigh ? "Source Active (+5V HIGH / Logic 1)" : "Source Grounded (0V LOW / Logic 0)");
    }

    public void toggle() {
        setActiveHigh(!this.activeHigh);
    }

    public double getVoltageVolts() { return voltageVolts; }

    @Override
    public boolean evaluate(Map<String, Boolean> memo, Set<String> visited) {
        memo.put(getId(), activeHigh);
        setCurrentOutputState(activeHigh);
        setValidConnection(true);
        return activeHigh;
    }

    @Override
    public String toBooleanExpression(Set<String> visited) {
        return getName();
    }

    @Override
    public String getEducationalNote() {
        return "DC Digital Input Source (" + getName() + "): Outputs +5V (Logic 1) when switched ON, and 0V (Logic 0) when OFF.";
    }
}
