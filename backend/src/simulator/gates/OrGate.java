package simulator.gates;

import java.util.List;

/**
 * Concrete Gate: OR Gate (IC 7432)
 * Output is HIGH (1) if AT LEAST ONE input is HIGH (1).
 * Boolean Expression: Y = A + B
 */
public class OrGate extends LogicGate {
    public OrGate(String id, String name, double x, double y, int inputPinCount) {
        super(id, name, "OR", x, y, Math.max(2, inputPinCount), "IC 7432", "+");
    }

    @Override
    protected boolean computeGateLogic(List<Boolean> inputs) {
        for (boolean in : inputs) {
            if (in) return true;
        }
        return false;
    }

    @Override
    protected String formatExpression(List<String> inputExpressions) {
        return "(" + String.join(" + ", inputExpressions) + ")";
    }

    @Override
    public String getEducationalNote() {
        return "OR Gate (TTL IC 7432): Performs logical disjunction (Y = A + B). Output is HIGH (5V) when one or more inputs are HIGH.";
    }
}
