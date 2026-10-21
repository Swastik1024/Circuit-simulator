package simulator.gates;

import java.util.List;

/**
 * Concrete Gate: XOR Gate / Exclusive-OR (IC 7486)
 * Output is HIGH (1) when an ODD number of inputs are HIGH (inequality detector / modulo-2 sum).
 * Boolean Expression: Y = A ⊕ B
 */
public class XorGate extends LogicGate {
    public XorGate(String id, String name, double x, double y, int inputPinCount) {
        super(id, name, "XOR", x, y, Math.max(2, inputPinCount), "IC 7486", "⊕");
    }

    @Override
    protected boolean computeGateLogic(List<Boolean> inputs) {
        boolean parity = false;
        for (boolean in : inputs) {
            parity ^= in;
        }
        return parity;
    }

    @Override
    protected String formatExpression(List<String> inputExpressions) {
        return "(" + String.join(" ⊕ ", inputExpressions) + ")";
    }

    @Override
    public String getEducationalNote() {
        return "XOR Gate (TTL IC 7486): Exclusive-OR gate (Y = A ⊕ B). Outputs HIGH when inputs differ (odd parity); fundamental building block of binary adders.";
    }
}
