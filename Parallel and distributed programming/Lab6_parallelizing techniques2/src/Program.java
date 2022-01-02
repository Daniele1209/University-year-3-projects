import Entities.FindCycle;
import Entities.DirectedGraph;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Program {
    private static final int NODES = 1000;
    private static final int THREADS = 1000;


    public static void main(String[] args) throws InterruptedException {
        // Create a graph with N number of vertices
        DirectedGraph graph = DirectedGraph.randomGraph(NODES);
        execute(graph);
    }

    public static void execute(DirectedGraph graph) throws InterruptedException {
        long start = System.nanoTime();
        findHamiltonian(graph);
        System.out.println("Node number -> " + NODES + " done in " + (System.nanoTime() - start) / 1000000 + " ms");
    }

    public static void findHamiltonian(DirectedGraph graph) throws InterruptedException {
        // Declare new thread pool
        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        // Result as an array
        List<Integer> result = new ArrayList<>(graph.size());
        AtomicBoolean isDone = new AtomicBoolean(false);

        // Start the search from each of the nodes
        for (int idx = 0; idx < graph.size(); idx++){
            pool.submit(new FindCycle(idx, graph, result, isDone));
        }
        // Terminate the threads
        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);
    }
}
