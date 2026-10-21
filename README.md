# ElectroLogic OOP — First-Year Electrical & Digital Logic Circuit Simulator

A full-stack **Object-Oriented Programming (OOP) in Java** project designed for first-year engineering students to build, wire, and simulate digital & basic electrical logic circuits with real-time current animation and LED bulb output verification.

## Project Architecture (Separate Frontend & Java Backend)

```
circuit_simulator_project/
├── backend/
│   └── src/simulator/
│       ├── Main.java                              # CLI self-test + HTTP Server launcher
│       ├── model/
│       │   ├── Evaluatable.java                   # Interface for signal & Boolean algebra evaluation
│       │   ├── CircuitNode.java                   # Abstract base class for all circuit components
│       │   ├── InputSource.java                   # User-named +5V/0V toggleable DC source
│       │   ├── LedBulb.java                       # Output LED bulb with connection & glow validation
│       │   └── WireConnection.java                # Directed electrical wire with current-flow state
│       ├── gates/
│       │   ├── LogicGate.java                     # Abstract gate class (Template Method pattern)
│       │   ├── GateFactory.java                   # Factory pattern creating concrete gates
│       │   ├── AndGate.java                       # IC 7408 (Y = A · B)
│       │   ├── OrGate.java                        # IC 7432 (Y = A + B)
│       │   ├── NotGate.java                       # IC 7404 (Y = ¬A)
│       │   ├── NandGate.java                      # IC 7400 (Y = ¬(A · B))
│       │   ├── NorGate.java                       # IC 7402 (Y = ¬(A + B))
│       │   ├── XorGate.java                       # IC 7486 (Y = A ⊕ B)
│       │   └── XnorGate.java                      # IC 74266 (Y = A ⊙ B)
│       ├── engine/
│       │   └── CircuitBoard.java                  # Graph evaluation, cycle detection & Truth Table engine
│       ├── exceptions/
│       │   └── CircuitValidationException.java    # Custom runtime exception for wiring errors
│       └── server/
│           └── CircuitSimulatorServer.java        # Zero-dependency JDK HttpServer REST API (/api/simulate)
├── frontend/
│   └── index.html                                 # Interactive Drag-and-Drop Circuit Simulator UI
├── OOP_VIVA_GUIDE.md                              # Complete OOP concepts breakdown for project defense
├── run.sh                                         # Linux / macOS one-step compile & run script
└── run.bat                                        # Windows one-step compile & run script
```

## How to Run

### Option 1: Full-Stack Mode (Java Backend Server + Frontend)
Requires any standard **JDK (Java 11 or newer)** — uses standard library `com.sun.net.httpserver.HttpServer` so **no external Maven/Gradle jars are required**.

**On Windows:**
```cmd
run.bat
```

**On Linux / macOS:**
```bash
chmod +x run.sh
./run.sh
```

Then open **http://localhost:8080** in your browser! To run only the CLI verification demo without starting the web server:
```bash
java -cp backend/out simulator.Main --demo-only
```

### Option 2: Direct Browser Mode
You can also open `frontend/index.html` directly in any browser. It automatically connects to `http://localhost:8080/api/simulate` when the Java server is active, or seamlessly executes the mirrored client-side OOP simulation engine when offline.
