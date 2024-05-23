import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Represents a single rest stop on the mountain
class RestStop implements Comparable<RestStop> {
    private String label;
    private List<String> supplies;
    private List<String> obstacles;

    public RestStop(String label, List<String> supplies, List<String> obstacles) {
        this.label = label;
        this.supplies = supplies;
        this.obstacles = obstacles;
    }

    public String getLabel() {
        return label;
    }

    public List<String> getSupplies() {
        return supplies;
    }

    public List<String> getObstacles() {
        return obstacles;
    }

    @Override
    public int compareTo(RestStop other) {
        return this.label.compareTo(other.label);
    }
}

// Represents the hiker traveling down the mountain
class Hiker {
    private List<String> supplies;

    public Hiker(List<String> initialSupplies) {
        this.supplies = new ArrayList<>(initialSupplies);
    }

    public List<String> getSupplies() {
        return supplies;
    }

    // Method to consume supplies based on obstacles encountered
    public void consumeSupplies(List<String> obstacles) {
        for (String obstacle : obstacles) {
            if (obstacle.equals("fallen tree") && supplies.contains("axe")) {
                supplies.remove("axe"); // Using the axe consumes it
            } else if (obstacle.equals("river") && supplies.contains("raft")) {
                supplies.remove("raft"); // Using the raft consumes it
            }
        }
    }
}

// Node class for the binary search tree
class BSTNode<T extends Comparable<T>> {
    T data;
    BSTNode<T> left, right;

    public BSTNode(T data) {
        this.data = data;
        left = right = null;
    }
}

// Generic Binary Search Tree class
class BST<T extends Comparable<T>> {
    protected BSTNode<T> root;

    public BST() {
        root = null;
    }

    // Method to insert a node into the BST
    public void insert(T data) {
        root = insertRec(root, data);
    }

    private BSTNode<T> insertRec(BSTNode<T> root, T data) {
        if (root == null) {
            root = new BSTNode<>(data);
            return root;
        }
        if (data.compareTo(root.data) < 0)
            root.left = insertRec(root.left, data);
        else if (data.compareTo(root.data) > 0)
            root.right = insertRec(root.right, data);
        return root;
    }

    // Methods for inorder, preorder, and postorder traversal
    public void inorder() {
        inorderRec(root);
    }

    private void inorderRec(BSTNode<T> root) {
        if (root != null) {
            inorderRec(root.left);
            System.out.print(root.data + " ");
            inorderRec(root.right);
        }
    }

    public void preorder() {
        preorderRec(root);
    }

    private void preorderRec(BSTNode<T> root) {
        if (root != null) {
            System.out.print(root.data + " ");
            preorderRec(root.left);
            preorderRec(root.right);
        }
    }

    public void postorder() {
        postorderRec(root);
    }

    private void postorderRec(BSTNode<T> root) {
        if (root != null) {
            postorderRec(root.left);
            postorderRec(root.right);
            System.out.print(root.data + " ");
        }
    }
}

// Class representing the mountain (inherits from BST)
class BSTMountain extends BST<RestStop> {
    // Additional methods specific to mountain representation can be added here
}

// Main class responsible for parsing command line arguments, reading input files, and orchestrating the exploration of the mountain
public class MountainClimb {
    public static void main(String[] args) {
        // Validate command line arguments
        if (args.length != 1) {
            System.out.println("Usage: java MountainClimb <input_file>");
            return;
        }

        String filename = args[0];
        List<RestStop> restStops = new ArrayList<>();

        // Read and parse input file
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+", 2); // Split only on the first whitespace
                if (parts.length < 2) {
                    System.out.println("Invalid input format: " + line);
                    continue; // Skip this line and continue to the next one
                }
                String label = parts[0];
                List<String> supplies = Arrays.asList(parts[1].trim().split("\\s+"));
                RestStop restStop = new RestStop(label, supplies, new ArrayList<>()); // No obstacles in this case
                restStops.add(restStop);
            }
        } catch (IOException e) {
            System.out.println("Error reading the input file: " + e.getMessage());
            return;
        }

        // Build the binary search tree representing the mountain
        BSTMountain mountain = new BSTMountain();
        for (RestStop restStop : restStops) {
            mountain.insert(restStop);
        }

        // Explore the mountain and determine safe paths down
        exploreMountain(mountain);
    }

    private static void exploreMountain(BSTMountain mountain) {
        // Initialize a StringBuilder to store the current path
        StringBuilder currentPath = new StringBuilder();

        // Start the recursive exploration from the root of the mountain
        explorePath(mountain.root, currentPath);
    }

    private static void explorePath(BSTNode<RestStop> node, StringBuilder currentPath) {
        if (node == null) {
            return;
        }
    
        // Append the current node's label to the path
        currentPath.append(node.data.getLabel()).append(" ");
    
        // Check if we have reached a leaf node (bottom of the mountain)
        if (node.left == null && node.right == null) {
            // Check if the last RestStop's label in the path contains either "M" or "O"
            String[] labels = currentPath.toString().trim().split("\\s+");
            String lastLabel = labels[labels.length - 1]; // Get the last label
            if (lastLabel.contains("M") || lastLabel.contains("O")) {
                // Output the current path as it represents a complete path down the mountain
                System.out.println(currentPath.toString().trim());
            }
        } else {
            // Continue exploring the left and right subtrees recursively
            explorePath(node.left, currentPath);
            explorePath(node.right, currentPath);
        }
    
        // Remove the last added node's label from the path to backtrack
        currentPath.delete(currentPath.length() - node.data.getLabel().length() - 1, currentPath.length());
    }
    
}