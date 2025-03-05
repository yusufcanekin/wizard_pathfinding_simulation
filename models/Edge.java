package models;

/**
 * Represents an edge in a graph, connecting a source node to a destination node
 * with a specified travel time.
 */
public class Edge {
    private Node destinationNode; // The node this edge points to
    private double travelTime; // The travel time to the destination node

    /**
     * Constructs an edge with a specified destination node and travel time.
     *
     * @param destinationNode the node this edge points to
     * @param travelTime      the travel time to the destination node
     */
    public Edge(Node destinationNode, double travelTime) {
        this.destinationNode = destinationNode;
        this.travelTime = travelTime;
    }

    /**
     * Gets the destination node of this edge.
     *
     * @return the destination node
     */
    public Node getDestinationNode() {
        return this.destinationNode;
    }

    /**
     * Gets the travel time to the destination node. If the destination node has been discovered,
     * the travel time is set to {@code Double.MAX_VALUE}, indicating it is impassable.
     *
     * @return the travel time to the destination node, or {@code Double.MAX_VALUE} if the node is discovered
     */
    public double getTravelTime() {
        // If the destination node is discovered, return an impassable travel time
        if (destinationNode.isDiscovered) {
            return Double.MAX_VALUE;
        }
        return this.travelTime;
    }

    /**
     * Gets the travel time to the destination node based on the specified node type. This function is being used
     * while calculating best option for wizard. Even if node is discovered, if its type is same as given, then it
     * is being treated as a 0 type node.
     *
     * @param nodeType the type of the node to check against
     * @return the travel time to the destination node, or {@code Double.MAX_VALUE} if the conditions are not met
     */
    public double getTravelTime(int nodeType) {
        // If the destination node is discovered and its type does not match, return an impassable travel time
        if (destinationNode.isDiscovered && destinationNode.getNodeType() != nodeType) {
            return Double.MAX_VALUE;
        }
        return this.travelTime;
    }
}
