package actions;

import data_structures.HashTable;
import data_structures.MinHeap;
import models.Edge;
import models.Node;
import models.NodeDistance;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Contains static methods for performing various actions such as finding shortest paths,
 * discovering nodes within a radius, and managing wizard choices in the magical land.
 */
public class Actions {

    /**
     * Finds the shortest path between the initial node and the destination node using Dijkstra's algorithm.
     *
     * @param initialNode     the starting node
     * @param destinationNode the destination node
     * @return a list of nodes representing the shortest path, or null if no path exists
     */
    public static ArrayList<Node> findShortestPath(Node initialNode, Node destinationNode) {
        MinHeap<NodeDistance> minHeap = new MinHeap<>(100); // Priority queue for storing the shortest distances
        HashTable<Node, Double> distances = new HashTable<>(); // Maps nodes to their shortest distances
        HashTable<Node, Node> previous = new HashTable<>(); // Maps nodes to their previous nodes in the path
        HashTable<Node, Integer> visited = new HashTable<>(); // Tracks visited nodes

        distances.put(initialNode, 0.0); // Distance to the initial node is 0
        minHeap.insert(new NodeDistance(initialNode, 0.0));

        while (!minHeap.isEmpty()) {
            NodeDistance currentNodeDistance = minHeap.getMin();
            Node currentNode = currentNodeDistance.getNode();

            if (visited.containsKey(currentNode)) continue; // Skip already visited nodes
            visited.put(currentNode, currentNode.getNodeType());

            if (currentNode.equals(destinationNode)) {
                // Backtrack to construct the path
                ArrayList<Node> path = new ArrayList<>();
                Node current = destinationNode;

                while (current != null) {
                    path.add(current); // Insert nodes at the beginning
                    current = previous.get(current);
                }
                return path;
            }

            // Evaluate all neighbors of the current node
            for (Edge edge : currentNode.getEdges()) {
                Node neighbor = edge.getDestinationNode();
                double newDistance = distances.get(currentNode) + edge.getTravelTime();

                if (newDistance < distances.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    distances.put(neighbor, newDistance); // Update distance
                    previous.put(neighbor, currentNode); // Update previous node
                    minHeap.insert(new NodeDistance(neighbor, newDistance));
                }
            }
        }
        return null; // No path found
    }

    /**
     * Finds the shortest path between the initial node and destination node, treating
     * nodes of the specified type as always passable regardless of their current state.
     *
     * @param initialNode     the starting node
     * @param destinationNode the destination node
     * @param nodeType        the type of nodes to consider for traversal
     * @return the shortest path distance, or 0.0 if no path exists
     */
    public static double findShortestPath(Node initialNode, Node destinationNode, int nodeType) {
        MinHeap<NodeDistance> minHeap = new MinHeap<>(100);
        HashTable<Node, Double> distances = new HashTable<>();
        HashTable<Node, Node> previous = new HashTable<>();
        HashTable<Node, Integer> visited = new HashTable<>();

        distances.put(initialNode, 0.0);
        minHeap.insert(new NodeDistance(initialNode, 0.0));

        while (!minHeap.isEmpty()) {
            NodeDistance currentNodeDistance = minHeap.getMin();
            Node currentNode = currentNodeDistance.getNode();

            if (visited.containsKey(currentNode)) continue;
            visited.put(currentNode, currentNode.getNodeType());

            if (currentNode.equals(destinationNode)) {
                return distances.get(currentNode);
            }

            for (Edge edge : currentNode.getEdges()) {
                Node neighbor = edge.getDestinationNode();
                double newDistance = distances.get(currentNode) + edge.getTravelTime(nodeType);

                if (newDistance < distances.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    distances.put(neighbor, newDistance);
                    previous.put(neighbor, currentNode);
                    minHeap.insert(new NodeDistance(neighbor, newDistance));
                }
            }
        }
        return 0.0;
    }

    /**
     * Discovers nodes within a given radius of a center node and updates their visibility.
     *
     * @param centerNode the center node
     * @param radius     the radius within which nodes should be discovered
     * @param grid       the grid of nodes
     * @return a hash table of discovered nodes mapped to their types
     */
    public static HashTable<Node, Integer> getAdjacentNodesWithinRadius(Node centerNode, double radius, Node[][] grid) {
        HashTable<Node, Integer> visibleNodes = new HashTable<>();
        int centerX = centerNode.getxCoordinate();
        int centerY = centerNode.getyCoordinate();

        // Determine the boundaries of the search space
        int minX = Math.max(0, (int) Math.floor(centerX - radius));
        int maxX = Math.min(grid.length - 1, (int) Math.ceil(centerX + radius));
        int minY = Math.max(0, (int) Math.floor(centerY - radius));
        int maxY = Math.min(grid[0].length - 1, (int) Math.ceil(centerY + radius));

        // Search within the determined boundaries
        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                Node node = grid[i][j];
                if (node != null) {
                    double distance = Math.sqrt(Math.pow(node.getxCoordinate() - centerX, 2) +
                            Math.pow(node.getyCoordinate() - centerY, 2));
                    if (distance <= radius && node.getNodeType() > 1 && !node.isDiscovered()) {
                        node.discoverNode();
                        visibleNodes.put(node, node.getNodeType());
                    }
                }
            }
        }

        return visibleNodes;
    }

    /**
     * Attempts to move from the initial node to the destination node, revealing obstacles along the way.
     *
     * @param initialNode     the starting node
     * @param destinationNode the destination node
     * @param radius          the visibility radius
     * @param land            the grid of nodes
     * @param objectiveCount  the objective number
     * @param writer          the writer to log actions
     * @return the last reachable node in the path
     * @throws IOException if an error occurs while writing to the output
     */
    public static Node reachToObjective(Node initialNode, Node destinationNode, int radius,
                                        Node[][] land, int objectiveCount, BufferedWriter writer) throws IOException {
        ArrayList<Node> path = findShortestPath(initialNode, destinationNode);

        for (int i = path.size() - 2; i >= 0; i--) {
            Node node = path.get(i);
            writer.write(String.format("Moving to %d-%d\n", node.getxCoordinate(), node.getyCoordinate()));
            HashTable<Node, Integer> nodesInRadius = getAdjacentNodesWithinRadius(node, radius, land);

            for (int j = i; j >= 0; j--) {
                if (nodesInRadius.containsKey(path.get(j))) {
                    writer.write("Path is impassable!\n");
                    return node;
                }
            }

            if (node.equals(destinationNode)) {
                writer.write(String.format("Objective %s reached!\n", objectiveCount));
            }
        }
        return destinationNode;
    }

    /**
     * Determines the best choice offered by the wizard for making traversal easier.
     *
     * @param options         the wizard's options as an array of strings
     * @param initialNode     the starting node
     * @param destinationNode the destination node
     * @return the best choice for traversal
     */
    public static int chooseForWizard(String[] options, Node initialNode, Node destinationNode) {
        int bestChoice = 0;
        double bestPathDistance = Double.MAX_VALUE;

        for (int i = 2; i < options.length; i++) {
            double pathDistance = findShortestPath(initialNode, destinationNode, Integer.parseInt(options[i]));
            if (pathDistance < bestPathDistance) {
                bestPathDistance = pathDistance;
                bestChoice = Integer.parseInt(options[i]);
            }
        }
        return bestChoice;
    }

    /**
     * Updates the state of nodes after a wizard's choice is made.
     *
     * @param choice                  the type of nodes to update
     * @param nodesWithTwoOrMoreType a hash table of nodes grouped by type
     */
    public static void updateAfterChoice(Integer choice, HashTable<Integer, ArrayList<Node>> nodesWithTwoOrMoreType) {
        if (nodesWithTwoOrMoreType.get(choice) != null) {
            ArrayList<Node> nodesList = nodesWithTwoOrMoreType.get(choice);
            for (Node node : nodesList) {
                node.updateAfterChoice();
            }
            nodesWithTwoOrMoreType.remove(choice);
        }
    }
}
