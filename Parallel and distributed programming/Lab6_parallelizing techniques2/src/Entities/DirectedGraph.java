package Entities;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static java.util.Collections.shuffle;

public final class DirectedGraph {
    private final List<Integer> nodes;
    private final List<List<Integer>> graph_store;

    DirectedGraph(int nodeCount) {
        this.nodes = new ArrayList<>();
        this.graph_store = new ArrayList<>(nodeCount);
        for (int idx = 0; idx < nodeCount; idx++) {
            this.nodes.add(idx);
            this.graph_store.add(new ArrayList<>());
        }
    }

    public int size() {
        return this.graph_store.size();
    }

    public void addEdge(int first_node, int second_node) {
        this.graph_store.get(first_node).add(second_node);
    }

    public List<Integer> getNodes(){
        return nodes;
    }

    public List<Integer> getNeighbours(int node) {
        return this.graph_store.get(node);
    }

    public static DirectedGraph randomGraph(int size) {
        DirectedGraph resulted_graph = new DirectedGraph(size);
        List<Integer> nodes = resulted_graph.getNodes();
        // Use shuffle function to randomize the node order
        shuffle(nodes);

        // Connect nodes and keep in mind the last one in the list
        for (int idx = 1; idx < nodes.size(); idx++){
            resulted_graph.addEdge(nodes.get(idx - 1),  nodes.get(idx));
        }
        resulted_graph.addEdge(nodes.get(nodes.size() -1), nodes.get(0));

        // Add random edges between nodes
        // sqrt of size amount of edges
        Random random = new Random();
        for (int idx = 0; idx < Math.sqrt(size); idx++){
            resulted_graph.addEdge(random.nextInt(size - 1), random.nextInt(size - 1));
        }
        return resulted_graph;
    }

}
