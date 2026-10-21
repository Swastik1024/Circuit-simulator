package simulator.model;

/**
 * OOP Concept: Encapsulation & Association
 * Represents a directed electrical wire connecting the output pin of a source node
 * to a specific input pin index of a target node.
 */
public class WireConnection {
    private final String id;
    private final CircuitNode sourceNode;
    private final CircuitNode targetNode;
    private final int targetPinIndex;
    private boolean carryingCurrent;
    private boolean validPath;

    public WireConnection(String id, CircuitNode sourceNode, CircuitNode targetNode, int targetPinIndex) {
        this.id = id;
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
        this.targetPinIndex = targetPinIndex;
        this.carryingCurrent = false;
        this.validPath = true;
    }

    public String getId() { return id; }
    public CircuitNode getSourceNode() { return sourceNode; }
    public CircuitNode getTargetNode() { return targetNode; }
    public int getTargetPinIndex() { return targetPinIndex; }

    public boolean isCarryingCurrent() { return carryingCurrent; }
    public void setCarryingCurrent(boolean carryingCurrent) { this.carryingCurrent = carryingCurrent; }

    public boolean isValidPath() { return validPath; }
    public void setValidPath(boolean validPath) { this.validPath = validPath; }

    public double getVoltage() {
        return carryingCurrent ? 5.0 : 0.0;
    }
}
