package operations;

import adt.Matrix;
import entities.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ThreadPoolApproach {
    public static void run(Matrix first_matrix, Matrix second_matrix, int threads_nb, String task, Matrix final_matrix) throws InterruptedException {
        ExecutorService exec_service = Executors.newFixedThreadPool(threads_nb);

        for (int idx = 0; idx <= threads_nb-1; idx++)
            exec_service.submit(Utility.threadInitialization(idx, first_matrix, second_matrix, final_matrix, threads_nb, task));

        exec_service.shutdown();

        if (!exec_service.awaitTermination(3000, TimeUnit.SECONDS)) {
            exec_service.shutdownNow();
        }
    }
}
