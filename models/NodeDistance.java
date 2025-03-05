package models;

/**
 * Represents a node with an associated distance, used for Dijkstra's algorithm.
 * This class implements {@link Comparable} to allow comparison based on the distance value.
 */
public class NodeDistance implements Comparable<NodeDistance> {
    private Node node; // The node associated with this distance
    private double destination; // The distance value for this node

    /**
     * Constructs a NodeDistance object with a specified node and distance.
     *
     * @param node        the node being evaluated
     * @param destination the distance associated with the node
     */
    public NodeDistance(Node node, double destination) {
        this.destination = destination;
        this.node = node;
    }

    /**
     * Compares this NodeDistance object with another based on the distance value.
     * Nodes with smaller distances are considered "less than" nodes with larger distances.
     *
     * @param nodeDestination the other NodeDistance object to compare against
     * @return a positive integer, zero, or a negative integer as this object's
     *         distance is greater than, equal to, or less than the specified object's distance
     */
    @Override
    public int compareTo(NodeDistance nodeDestination) {
        if (this.destination > nodeDestination.destination) {
            return 1;
        } else if (this.destination < nodeDestination.destination) {
            return -1;
        }
        return 0;
    }

    /**
     * Retrieves the node associated with this NodeDistance object.
     *
     * @return the node
     */
    public Node getNode() {
        return this.node;
    }
}
