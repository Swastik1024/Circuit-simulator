package simulator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * OOP Concepts: Abstraction, Encapsulation, Inheritance Base Class
 * Abstract base class representing any placeable component on the circuit canvas
 * (Input Voltage Source, Logic Gate, Output LED Bulb, or Probe).
 */
public abstract class CircuitNode implements Evaluatable {
    private final String id;
    private String name;
    private final String type;
    private double x;
    private double y;
    private final int inputPinCount;
    private final int outputPinCount;

    // Stores incoming wire connections indexed by input pin slot
    private final List<WireConnection> incomingWires;
    private boolean currentOutputState;
    private boolean validConnection;
    private String diagnosticMessage;

    protected CircuitNode(String id, String name, String type, double x, double y, int inputPinCount, int outputPinCount) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Component ID cannot be null or empty.");
        }
        this.id = id.trim();
        this.name = (name == null || name.trim().isEmpty()) ? id : name.trim();
        this.type = type;
        this.x = x;
        this.y = y;
        this.inputPinCount = Math.max(0, inputPinCount);
        this.outputPinCount = Math.max(0, outputPinCount);
        this.incomingWires = new ArrayList<>();
        this.currentOutputState = false;
        this.validConnection = true;
        this.diagnosticMessage = "OK";
    }

    // Encapsulated Getters & Setters
    public String getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name.trim();
        }
    }

    public String getType() { return type; }

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public int getInputPinCount() { return inputPinCount; }
    public int getOutputPinCount() { return outputPinCount; }

    public List<WireConnection> getIncomingWires() { return incomingWires; }

    public void clearIncomingWires() {
        this.incomingWires.clear();
    }

    public void addIncomingWire(WireConnection wire) {
        if (wire != null) {
            this.incomingWires.add(wire);
        }
    }

    public boolean getCurrentOutputState() { return currentOutputState; }
    public void setCurrentOutputState(boolean state) { this.currentOutputState = state; }

    public boolean isValidConnection() { return validConnection; }
    public void setValidConnection(boolean validConnection) { this.validConnection = validConnection; }

    public String getDiagnosticMessage() { return diagnosticMessage; }
    public void setDiagnosticMessage(String diagnosticMessage) { this.diagnosticMessage = diagnosticMessage; }

    /**
     * Polymorphic method returning the educational description & IC reference for 1st-year students.
     */
    public abstract String getEducationalNote();
}
