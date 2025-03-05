package models;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a node in the graph, characterized by its coordinates, type, and edges.
 * Each node can maintain a list of edges that connect it to other nodes.
 */
public class Node {
    int xCoordinate; // The x-coordinate of the node
    int yCoordinate; // The y-coordinate of the node
    int nodeType; // The type of the node (0 for passable, 1 for impassable, 2 and above for undiscovered impassable)
    boolean isDiscovered = false; // Indicates if the node has been discovered
    private ArrayList<Edge> edges; // List of edges connecting this node to others

    /**
     * Constructs a new node with the specified coordinates and type.
     *
     * @param xCoordinate the x-coordinate of the node
     * @param yCoordinate the y-coordinate of the node
     * @param nodeType    the type of the node
     */
    public Node(int xCoordinate, int yCoordinate, int nodeType) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.nodeType = nodeType;
        this.edges = new ArrayList<>();
    }

    /**
     * Adds an edge from this node to a destination node with a specified travel time.
     *
     * @param destinationNode the node this edge connects to
     * @param travelTime      the travel time to the destination node
     */
    public void addEdge(Node destinationNode, double travelTime) {
        Edge edge = new Edge(destinationNode, travelTime);
        this.edges.add(edge);
    }

    /**
     * Retrieves the list of edges connected to this node.
     *
     * @return an ArrayList of edges
     */
    public ArrayList<Edge> getEdges() {
        return this.edges;
    }

    /**
     * Retrieves the type of the node.
     *
     * @return the type of the node
     */
    public int getNodeType() {
        return this.nodeType;
    }

    /**
     * Retrieves the x-coordinate of the node.
     *
     * @return the x-coordinate
     */
    public int getxCoordinate() {
        return xCoordinate;
    }

    /**
     * Retrieves the y-coordinate of the node.
     *
     * @return the y-coordinate
     */
    public int getyCoordinate() {
        return yCoordinate;
    }

    /**
     * Checks equality of this node with another object. Nodes are considered equal
     * if their x and y coordinates match.
     *
     * @param obj the object to compare
     * @return {@code true} if the nodes have the same coordinates, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Node)) {
            return false;
        }
        Node node = (Node) obj;
        return this.xCoordinate == node.xCoordinate && this.yCoordinate == node.yCoordinate;
    }

    /**
     * Computes a hash code for this node based on its coordinates.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(xCoordinate, yCoordinate);
    }

    /**
     * Marks this node as discovered, updating its state accordingly.
     */
    public void discoverNode() {
        this.isDiscovered = true;
    }

    /**
     * Updates the node after a wizard's choice is made. Resets the node type to 0
     * and marks the node as undiscovered.
     */
    public void updateAfterChoice() {
        this.nodeType = 0;
        this.isDiscovered = false;
    }

    public boolean isDiscovered() {
        return isDiscovered;
    }
}
