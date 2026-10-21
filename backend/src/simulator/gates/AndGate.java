package simulator.gates;

import java.util.List;

/**
 * Concrete Gate: AND Gate (IC 7408)
 * Output is HIGH (1) if and only if ALL inputs are HIGH (1).
 * Boolean Expression: Y = A · B
 */
public class AndGate extends LogicGate {
    public AndGate(String id, String name, double x, double y, int inputPinCount) {
        super(id, name, "AND", x, y, Math.max(2, inputPinCount), "IC 7408", "·");
    }

    @Override
    protected boolean computeGateLogic(List<Boolean> inputs) {
        for (boolean in : inputs) {
            if (!in) return false;
        }
        return true;
    }

    @Override
    protected String formatExpression(List<String> inputExpressions) {
        return "(" + String.join(" · ", inputExpressions) + ")";
    }

    @Override
    public String getEducationalNote() {
        return "AND Gate (TTL IC 7408): Performs logical conjunction (Y = A · B). Output is HIGH (5V) only when every input is HIGH.";
    }
}
