package simulator.exceptions;

/**
 * OOP Concept: Custom Exception Handling
 * Thrown when a circuit contains invalid wiring, unconnected input pins,
 * or combinational feedback loops (short circuits).
 */
public class CircuitValidationException extends RuntimeException {
    private final String componentId;
    private final String errorCode;

    public CircuitValidationException(String componentId, String errorCode, String message) {
        super(message);
        this.componentId = componentId;
        this.errorCode = errorCode;
    }

    public String getComponentId() {
        return componentId;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
