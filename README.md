# Wizard Pathfinding Simulation

## Project Overview
This project implements a wizard-based pathfinding simulation using Dijkstra's algorithm. It models a grid-based environment where a wizard navigates through a magical land, encountering obstacles, making decisions, and receiving assistance to optimize the journey. The system is built with a custom `HashTable` for efficient data storage and a `MinHeap` for shortest path calculations.

## Features
- **Pathfinding:**
  - Computes the shortest path using Dijkstra's algorithm.
  - Supports alternative pathfinding when magical assistance is available.
- **Obstacle Discovery:**
  - Reveals undiscovered obstacles within a given radius.
- **Wizard Assistance:**
  - Provides decision-making support to overcome impassable nodes.
  - Updates the state of nodes after receiving wizard help.
- **Grid-based Representation:**
  - Uses a 2D grid to represent the terrain.
  - Defines nodes and edges to model movement restrictions.

## Project Structure
```
📂 src/
 ├── actions/              # Contains core actions for pathfinding and wizard choices.
 │   └── Actions.java      # Implements pathfinding, obstacle detection, and wizard interactions.
 ├── data_structures/      # Custom data structures for optimization.
 │   ├── HashTable.java    # A hash table with separate chaining.
 │   ├── MinHeap.java      # A min-heap implementation for priority-based path selection.
 ├── models/               # Core classes for representing nodes and edges.
 │   ├── Node.java         # Represents a node in the grid.
 │   ├── Edge.java         # Represents an edge connecting two nodes.
 │   ├── NodeDistance.java # Helper class for Dijkstra's algorithm.
 ├── Main.java             # Entry point of the application.
```

## Usage
### Compilation
To compile the project, navigate to the `src` directory and run:
```sh
javac -d out *.java actions/*.java data_structures/*.java models/*.java
```

### Running the Program
Run the program with input files for nodes, edges, and objectives:
```sh
java -cp out Main nodes.txt edges.txt objectives.txt output.txt
```
- `nodes.txt`: Contains the grid size and node definitions.
- `edges.txt`: Defines edges and travel times between nodes.
- `objectives.txt`: Specifies the wizard's objectives and decisions.
- `output.txt`: Stores the simulation results.

### Input File Format
#### Nodes File (`nodes.txt`)
```
X Y            # Grid dimensions (X by Y)
X1 Y1 Type     # Node at (X1, Y1) with type
X2 Y2 Type     # More nodes...
```
#### Edges File (`edges.txt`)
```
X1-Y1,X2-Y2 TravelTime  # Edge between (X1,Y1) and (X2,Y2) with given travel time
```
#### Objectives File (`objectives.txt`)
```
Radius         # Visibility radius
X1 Y1         # Initial position of the wizard
X2 Y2         # Destination node
Option1       # (Optional) Wizard's choice options
```

## Example Input
### `nodes.txt`
```
5 5
0 0 0
1 0 2
2 0 1
3 0 0
4 0 0
```
### `edges.txt`
```
0-0,1-0 2.5
1-0,2-0 3.0
2-0,3-0 1.5
3-0,4-0 1.0
```
### `objectives.txt`
```
2
0 0
4 0
```

## Example Output
```
Moving to 1-0
Moving to 3-0
Moving to 4-0
Objective 1 reached!
```

## Dependencies
This project is implemented in pure Java with no external dependencies.

## Authors
- **Developer:** Yusuf Can Ekin

## License
This project is open-source and can be modified as needed.

