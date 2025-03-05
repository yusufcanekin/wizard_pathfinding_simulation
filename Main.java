import data_structures.HashTable;
import models.Node;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static actions.Actions.*;

/**
 * This program processes nodes, edges, and objectives input files to create a map representation,
 * calculates optimal paths, handles obstacles, and performs the wizard's tasks.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(args[3]));

        File nodes = new File(args[0]);
        File edges = new File(args[1]);
        File objectives = new File(args[2]);


        Scanner nodesFile = new Scanner(nodes);

        Node[][] land = new Node[0][0];
        HashTable<Integer, ArrayList<Node>> nodesWithTwoOrMoreType = new HashTable<>();

        // Process the nodes file to create the land matrix.
        while (nodesFile.hasNextLine()) {
            String line = nodesFile.nextLine();
            String[] lineParts = line.split(" ");

            if (lineParts.length == 2) {
                // First line specifies the size of the grid.
                int xAxis = Integer.parseInt(lineParts[0]);
                int yAxis = Integer.parseInt(lineParts[1]);
                land = new Node[xAxis][yAxis];
            } else {
                // Remaining lines specify node properties.
                int xCoordinate = Integer.parseInt(lineParts[0]);
                int yCoordinate = Integer.parseInt(lineParts[1]);
                Integer nodeType = Integer.parseInt(lineParts[2]);
                Node node = new Node(xCoordinate, yCoordinate, nodeType);

                // If node type >= 2, store it in the hashtable
                // in order to adjust them efficiently after wizard's help.
                if (nodeType > 1) {
                    ArrayList<Node> nodesList = nodesWithTwoOrMoreType.getOrPut(nodeType);
                    nodesList.add(node);
                }
                land[xCoordinate][yCoordinate] = node;
            }
        }

        // Process the edges file to establish connections between nodes.
        Scanner edgesFile = new Scanner(edges);
        while (edgesFile.hasNextLine()) {
            String line = edgesFile.nextLine();
            String[] lineParts = line.split(" ");
            double travelTime = Double.parseDouble(lineParts[1]);
            String[] leftPart = lineParts[0].split(",");

            // Extract coordinates for the two nodes.
            String[] firstNodeCoordinates = leftPart[0].split("-");
            Node node1 = land[Integer.parseInt(firstNodeCoordinates[0])][Integer.parseInt(firstNodeCoordinates[1])];

            String[] secondNodeCoordinates = leftPart[1].split("-");
            Node node2 = land[Integer.parseInt(secondNodeCoordinates[0])][Integer.parseInt(secondNodeCoordinates[1])];

            // Make travel time between nodes infinite if either one of them is of type 1 (impassable).
            if (node1.getNodeType() == 1 || node2.getNodeType() == 1) {
                travelTime = Double.MAX_VALUE;
            }

            // Establish bidirectional edges between nodes.
            node1.addEdge(node2, travelTime);
            node2.addEdge(node1, travelTime);
        }

        // Process the objectives file.
        Scanner objectivesFile = new Scanner(objectives);

        // First line: radius of the map's visibility.
        String line = objectivesFile.nextLine();
        int radius = Integer.parseInt(line);

        // Second line: initial starting node coordinates.
        line = objectivesFile.nextLine();
        String[] lineParts = line.split(" ");
        int xCoordinateOfInitialNode = Integer.parseInt(lineParts[0]);
        int yCoordinateOfInitialNode = Integer.parseInt(lineParts[1]);

        Node initialNode = land[xCoordinateOfInitialNode][yCoordinateOfInitialNode];

        // Process each objective sequentially.
        int objectiveCount = 1;
        String[] options = null;

        while (objectivesFile.hasNextLine()) {
            line = objectivesFile.nextLine();
            lineParts = line.split(" ");
            int xCoordinateOfDestination = Integer.parseInt(lineParts[0]);
            int yCoordinateOfDestination = Integer.parseInt(lineParts[1]);
            Node destinationNode = land[xCoordinateOfDestination][yCoordinateOfDestination];

            if (options != null) {
                // Wizard offers a choice; process the options and update the map.
                int choice = chooseForWizard(options, initialNode, destinationNode);
                writer.write(String.format("Number %d is chosen!\n", choice));
                updateAfterChoice(choice, nodesWithTwoOrMoreType);
            }

            if (lineParts.length > 2) {
                // Update options if provided.
                options = lineParts;
            } else {
                options = null;
            }

            Node lastNode = initialNode;
            // Before calculating to route to the destination, first reveal nodes within radius.
            getAdjacentNodesWithinRadius(initialNode, radius, land);

            // Head to the destination node.
            while (lastNode != destinationNode) {
                lastNode = reachToObjective(lastNode, destinationNode, radius, land, objectiveCount, writer);
            }

            // Update initial node for the next objective.
            initialNode = destinationNode;
            objectiveCount++;
        }

        writer.close();
        edgesFile.close();
        nodesFile.close();
        objectivesFile.close();
    }
}
