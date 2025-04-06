# A* 8-Puzzle Solver

This project is a Java-based solver for the classic 8-puzzle problem, developed as part of an introductory Artificial Intelligence course assignment. It implements the A* search algorithm with multiple heuristic options to explore different goal conditions.

## Features

- A* search with three heuristics:
  - **DEF**: Default Manhattan distance to goal
  - **TOP**: Ensure top row is `[1,2,3]`
  - **SUM**: Any top row summing to 9
- Supports both random and manual puzzle inputs
- Tracks visited states, cost, and solution path
- Command-line interface for user interaction

## Technologies

- Java 8+
- Java Collections API
- Object-Oriented Programming
- AI search techniques (A*, heuristics)

## How to Run

Compile all `.java` files and run:

```bash
javac *.java
java TestAStar
