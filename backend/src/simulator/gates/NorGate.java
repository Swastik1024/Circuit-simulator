package simulator.gates;

import java.util.List;

/**
 * Concrete Gate: NOR Gate (IC 7402 - Universal Gate)
 * Output is HIGH (1) only when ALL inputs are LOW (0).
 * Boolean Expression: Y = ¬(A + B)
 */
public class NorGate extends LogicGate {
    public NorGate(String id, String name, double x, double y, int inputPinCount) {
        super(id, name, "NOR", x, y, Math.max(2, inputPinCount), "IC 7402", "↓");
    }

    @Override
    protected boolean computeGateLogic(List<Boolean> inputs) {
        for (boolean in : inputs) {
            if (in) return false;
        }
        return true;
    }

    @Override
    protected String formatExpression(List<String> inputExpressions) {
        return "¬(" + String.join(" + ", inputExpressions) + ")";
    }

    @Override
    public String getEducationalNote() {
        return "NOR Gate (TTL IC 7402): Universal gate equivalent to OR followed by NOT, Y = ¬(A + B). Output is HIGH only when all inputs are LOW (0V).";
    }
}
