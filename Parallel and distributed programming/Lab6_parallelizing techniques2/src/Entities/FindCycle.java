package Entities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class FindCycle implements Runnable {
    private final int startNode;
    private final DirectedGraph graph;
    private final List<Integer> result;
    private final AtomicBoolean isFound;
    private final List<Integer> path;
    private final Lock lock;


    public FindCycle(int startNode, DirectedGraph graph, List<Integer> result, AtomicBoolean isFound) {
        this.startNode = startNode;
        this.graph = graph;
        this.result = result;
        this.isFound = isFound;
        this.path = new ArrayList<>();
        this.lock = new ReentrantLock();
    }

    @Override
    public void run() {
        visitNodes(startNode);
    }

    private void visitNodes(int node) {
        path.add(node);
        // Check if we did not find the cycle yet
        if (!isFound.get()){
            if (path.size() == graph.size()) {
                // Check if our current node connects to our starting one
                if (graph.getNeighbours(node).contains(startNode)){
                    // This means that we found a cycle
                    isFound.set(true);
                    this.lock.lock();
                    result.clear();
                    result.addAll(this.path);
                    this.lock.unlock();
                }
                return;
            }
            // Check if any of our neighbours is in path, if not, we visit it
            // In case we did not find a cycle
            graph.getNeighbours(node).forEach(neighbour->{
                if (!this.path.contains(neighbour)){
                    visitNodes(neighbour);
                }
            });
        }
    }


}
