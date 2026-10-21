package simulator.gates;

import java.util.List;

/**
 * Concrete Gate: NOT Gate / Inverter (IC 7404)
 * Inverts its single input signal.
 * Boolean Expression: Y = ¬A (or A')
 */
public class NotGate extends LogicGate {
    public NotGate(String id, String name, double x, double y) {
        super(id, name, "NOT", x, y, 1, "IC 7404", "¬");
    }

    @Override
    protected boolean computeGateLogic(List<Boolean> inputs) {
        return !inputs.get(0);
    }

    @Override
    protected String formatExpression(List<String> inputExpressions) {
        return "¬" + inputExpressions.get(0);
    }

    @Override
    public String getEducationalNote() {
        return "NOT Gate / Inverter (TTL IC 7404): Performs logical complementation (Y = ¬A). Converts HIGH (5V) to LOW (0V) and vice versa.";
    }
}
