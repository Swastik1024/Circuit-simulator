package simulator.gates;

import java.util.List;

/**
 * Concrete Gate: NAND Gate (IC 7400 - Universal Gate)
 * Output is LOW (0) only when ALL inputs are HIGH (1).
 * Boolean Expression: Y = ¬(A · B)
 */
public class NandGate extends LogicGate {
    public NandGate(String id, String name, double x, double y, int inputPinCount) {
        super(id, name, "NAND", x, y, Math.max(2, inputPinCount), "IC 7400", "↑");
    }

    @Override
    protected boolean computeGateLogic(List<Boolean> inputs) {
        for (boolean in : inputs) {
            if (!in) return true;
        }
        return false;
    }

    @Override
    protected String formatExpression(List<String> inputExpressions) {
        return "¬(" + String.join(" · ", inputExpressions) + ")";
    }

    @Override
    public String getEducationalNote() {
        return "NAND Gate (TTL IC 7400): Universal gate equivalent to AND followed by NOT, Y = ¬(A · B). Any Boolean function can be built using only NAND gates.";
    }
}
