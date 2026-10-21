# Java OOP Project Viva & Evaluation Guide

Use this reference sheet during your Object-Oriented Programming (OOP) lab demonstration or project viva.

| OOP Pillar / Pattern | Where It Is Implemented in Your Java Backend | How It Works (Viva Explanation) |
| :--- | :--- | :--- |
| **1. Abstraction** | `Evaluatable.java` (Interface), `CircuitNode.java` (Abstract Class), `LogicGate.java` (Abstract Class) | Hides low-level recursive traversal details behind `evaluate(memo, visited)` and `computeGateLogic(inputs)`. `CircuitBoard` treats all components uniformly as `CircuitNode`. |
| **2. Inheritance** | `InputSource`, `LedBulb`, and `LogicGate` extend `CircuitNode`; `AndGate`, `OrGate`, `NotGate`, `NandGate`, `NorGate`, `XorGate`, `XnorGate` extend `LogicGate` | Common properties (`id`, `name`, `x`, `y`, `incomingWires`, `validConnection`) are inherited from `CircuitNode`, avoiding code duplication across 9 component types. |
| **3. Polymorphism** | Method overriding of `evaluate()`, `computeGateLogic()`, `formatExpression()`, and `getEducationalNote()` | At runtime, calling `node.evaluate()` dynamically dispatches to `InputSource.evaluate()`, `LogicGate.evaluate()`, or `LedBulb.evaluate()`. Inside `LogicGate`, `computeGateLogic()` dispatches to the specific gate's Boolean truth logic. |
| **4. Encapsulation** | Private fields (`private final String id`, `private boolean activeHigh`, `private boolean glowing`) with validated getters/setters in all model classes | Prevents external classes from corrupting internal node state (e.g., `setName()` rejects blank labels; `setActiveHigh()` automatically synchronizes voltage between `0.0V` and `5.0V`). |
| **5. Factory Pattern** | `GateFactory.createGate(...)` | Decouples the REST server and `CircuitBoard` from concrete gate constructors (`new AndGate(...)`, `new XorGate(...)`). |
| **6. Template Method Pattern** | `LogicGate.evaluate()` calling abstract `computeGateLogic()` | `LogicGate.evaluate()` implements the invariant algorithm (cycle detection, floating-pin check, wire current propagation) and delegates the gate-specific truth table calculation to subclasses. |
| **7. Exception Handling** | `CircuitValidationException.java` | Custom unchecked exception encapsulating `componentId` and `errorCode` for circuit errors such as unconnected pins or feedback loops. |
