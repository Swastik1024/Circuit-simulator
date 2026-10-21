package simulator.model;

import java.util.Map;
import java.util.Set;

/**
 * OOP Concept: Interface & Abstraction
 * Defines the contract for any electrical/logic component on the circuit board
 * that can evaluate its output signal state and Boolean expression.
 */
public interface Evaluatable {
    /**
     * Evaluates the logical/electrical state (true = 5V HIGH, false = 0V LOW).
     *
     * @param memo    Memoization map of already evaluated node IDs
     * @param visited Set of currently active nodes in the DFS stack (cycle detection)
     * @return true if 5V HIGH (1), false if 0V LOW (0)
     */
    boolean evaluate(Map<String, Boolean> memo, Set<String> visited);

    /**
     * Builds the symbolic Boolean algebra expression driving this component.
     *
     * @param visited Set of visited node IDs to prevent infinite recursion
     * @return Symbolic Boolean formula string (e.g., "(A · B)")
     */
    String toBooleanExpression(Set<String> visited);
}
