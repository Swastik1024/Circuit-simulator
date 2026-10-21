package simulator.gates;

import java.util.List;

/**
 * Concrete Gate: XNOR Gate / Exclusive-NOR (IC 74266)
 * Output is HIGH (1) when an EVEN number of inputs are HIGH (equality comparator).
 * Boolean Expression: Y = ¬(A ⊕ B) or A ⊙ B
 */
public class XnorGate extends LogicGate {
    public XnorGate(String id, String name, double x, double y, int inputPinCount) {
        super(id, name, "XNOR", x, y, Math.max(2, inputPinCount), "IC 74266", "⊙");
    }

    @Override
    protected boolean computeGateLogic(List<Boolean> inputs) {
        boolean parity = false;
        for (boolean in : inputs) {
            parity ^= in;
        }
        return !parity;
    }

    @Override
    protected String formatExpression(List<String> inputExpressions) {
        return "(" + String.join(" ⊙ ", inputExpressions) + ")";
    }

    @Override
    public String getEducationalNote() {
        return "XNOR Gate (TTL IC 74266): Exclusive-NOR / Equivalence gate (Y = A ⊙ B). Outputs HIGH when two inputs are equal (both 0 or both 1).";
    }
}
