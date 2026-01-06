# Competitive Snake Agent (Java)

A grid-based Snake agent written in Java for a competitive multi-snake environment.  
At each turn, the agent parses the game state (apple location, obstacles, snake bodies) and selects a move using shortest-path planning on a 50×50 grid.

This is an older undergraduate project focused on algorithmic decision-making rather than UI/game rendering.

---

## 🧠 Approach

### State Representation
- Builds a 50×50 occupancy grid (`visited`)
- Marks:
  - obstacles
  - enemy snake bodies
  - (and implicitly avoids collisions)

### Path Planning
- Runs **Breadth-First Search (BFS)** from the agent’s head to compute a distance field (`DJ`)
- When a path to the apple exists, the agent backtracks through the distance field to choose the next step toward the apple
- When no path is found, the agent falls back to a “best available” safe move based on reachable neighbors

---

## 📌 Notes / Limitations
- This implementation uses BFS (uniform-cost shortest paths). A heuristic search variant (e.g. A*) could be a natural extension.
- The provided environment framework is not included in this repository.

---

## 🏃 How to Run
This agent was designed to be plugged into the Wits Snake framework (extends `za.ac.wits.snake.DevelopmentAgent`).  
To run it, place `MyAgent.java` in the framework project, compile, and run using the framework’s standard driver/runner.
