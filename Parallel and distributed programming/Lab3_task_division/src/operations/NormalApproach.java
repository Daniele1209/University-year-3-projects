package operations;

import adt.Matrix;
import entities.Utility;

import java.util.ArrayList;
import java.util.List;

public class NormalApproach {

    public static void run(Matrix first_matrix, Matrix second_matrix, int threads_nb, String task, Matrix final_matrix) {
        // Declare the list of threads as an array list
        List<Thread> thread_list = new ArrayList<>();

        // add threads to list
        for (int idx = 0; idx <= threads_nb-1; idx++)
            thread_list.add(Utility.threadInitialization(idx, first_matrix, second_matrix, final_matrix, threads_nb, task));

        // start all threads
        for (Thread thread : thread_list) {
            thread.start();
        }
        // join the threads after execution
        for (Thread thread : thread_list) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
