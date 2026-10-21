package simulator.gates;

/**
 * OOP Design Pattern: Factory Method Pattern
 * Instantiates the appropriate concrete LogicGate subclass based on gate type string.
 */
public final class GateFactory {
    private GateFactory() {}

    public static LogicGate createGate(String type, String id, String name, double x, double y, int inputs) {
        if (type == null) {
            throw new IllegalArgumentException("Gate type cannot be null");
        }
        switch (type.trim().toUpperCase()) {
            case "AND":  return new AndGate(id, name, x, y, inputs);
            case "OR":   return new OrGate(id, name, x, y, inputs);
            case "NOT":  return new NotGate(id, name, x, y);
            case "NAND": return new NandGate(id, name, x, y, inputs);
            case "NOR":  return new NorGate(id, name, x, y, inputs);
            case "XOR":  return new XorGate(id, name, x, y, inputs);
            case "XNOR": return new XnorGate(id, name, x, y, inputs);
            default:
                throw new IllegalArgumentException("Unsupported logic gate type: " + type);
        }
    }
}
