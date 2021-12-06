package Entities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;



public class Operation {
    public static final int THREADS = 5;

    // Simple Sequential case
    public static Polynomial simpleSequential(Polynomial pol1, Polynomial pol2) {
        int coeffListLength = pol1.getDegree() + pol2.getDegree() + 1;
        List<Integer> coefficients_list = new ArrayList<>();
        for (int idx = 0; idx < coeffListLength; idx++) {
            coefficients_list.add(0);
        }

        for (int idx = 0; idx < pol1.getCoefficients().size(); idx++) {
            for (int idx2 = 0; idx2 < pol2.getCoefficients().size(); idx2++) {
                int index = idx + idx2;
                int value = pol1.getCoefficients().get(idx) * pol2.getCoefficients().get(idx2);
                coefficients_list.set(index, coefficients_list.get(index) + value);
            }
        }

        return new Polynomial(coefficients_list);
    }

    // Simple Threaded case
    public static Polynomial simpleThreaded(Polynomial pol1, Polynomial pol2) throws InterruptedException {
        int coeffListLength = pol1.getDegree() + pol2.getDegree() + 1;
        // Final polynomial initialization
        List<Integer> coefficients = IntStream.range(0, coeffListLength).mapToObj(i -> 0).collect(Collectors.toList());
        Polynomial result = new Polynomial(coefficients);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREADS);
        int step = result.getLength() / THREADS;
        if (step == 0) {
            step = 1;
        }
        for (int idx = 0; idx < result.getLength(); idx += step) {
            Task task = new Task(idx, idx + step, pol1, pol2, result);
            executor.execute(task);
        }
        // Terminate the executor
        executor.shutdown();
        executor.awaitTermination(40, TimeUnit.SECONDS);

        return result;
    }

    // Karatsuba Sequential case
    public static Polynomial karatsubaSequential(Polynomial pol1, Polynomial pol2) {
        // We do a simpleSequential if the degree is smaller than 2
        if (pol1.getDegree() < 2 || pol2.getDegree() < 2) {
            return simpleSequential(pol1, pol2);
        }

        int length = Math.max(pol1.getDegree(), pol2.getDegree()) / 2;
        Polynomial firstpol1 = new Polynomial(pol1.getCoefficients().subList(0, length));
        Polynomial firstpol2 = new Polynomial(pol2.getCoefficients().subList(0, length));
        Polynomial secondpol1 = new Polynomial(pol1.getCoefficients().subList(length, pol1.getLength()));
        Polynomial secondpol2 = new Polynomial(pol2.getCoefficients().subList(length, pol2.getLength()));

        // Recursively calculate the three products
        Polynomial prod1 = karatsubaSequential(firstpol1, firstpol2);
        Polynomial prod2 = karatsubaSequential(Polynomial.add(firstpol1, secondpol1), Polynomial.add(firstpol2, secondpol2));
        Polynomial prod3 = karatsubaSequential(secondpol1, secondpol2);

        // Combine the three products to get the final result
        return getFinalResult(prod1, prod2, prod3, length);
    }

    // Karatsuba Threaded case
    public static Polynomial karatsubaThreaded(Polynomial pol1, Polynomial pol2) throws ExecutionException, InterruptedException {
        // We do a simpleSequential if the degree is smaller than 2
        if (pol1.getDegree() < 2 || pol2.getDegree() < 2) {
            return karatsubaSequential(pol1, pol2);
        }

        int length = Math.max(pol1.getDegree(), pol2.getDegree()) / 2;
        Polynomial firstpol1 = new Polynomial(pol1.getCoefficients().subList(0, length));
        Polynomial firstpol2 = new Polynomial(pol2.getCoefficients().subList(0, length));
        Polynomial secondpol1 = new Polynomial(pol1.getCoefficients().subList(length, pol1.getLength()));
        Polynomial secondpol2 = new Polynomial(pol2.getCoefficients().subList(length, pol2.getLength()));
        // Declare an executor
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);

        // Recursively calculate the three products
        Future<Polynomial> future_prod1 = executor.submit(() -> karatsubaThreaded(firstpol1, firstpol2));
        Future<Polynomial> future_prod2 = executor.submit(() -> karatsubaThreaded(Polynomial.add(firstpol1, secondpol1), Polynomial.add(firstpol2, secondpol2)));
        Future<Polynomial> future_prod3 = executor.submit(() -> karatsubaThreaded(secondpol1, secondpol2));
        executor.shutdown();
        Polynomial prod1 = future_prod1.get();
        Polynomial prod2 = future_prod2.get();
        Polynomial prod3 = future_prod3.get();

        // Terminate the executor
        executor.awaitTermination(40, TimeUnit.SECONDS);
        // Combine the three products to get the final result
        return getFinalResult(prod1, prod2, prod3, length);
    }

    public static Polynomial getFinalResult(Polynomial prod1, Polynomial prod2, Polynomial prod3, int length) {
        // Combine results using formula P1*(1<<(2*sh)) + (P3 - P1 - P2)*(1<<sh) + P2;
        Polynomial first_result = Polynomial.addZeroValues(prod3, 2 * length);
        Polynomial second_result = Polynomial.addZeroValues(Polynomial.subtract(Polynomial.subtract(prod2, prod3), prod1), length);
        return Polynomial.add(Polynomial.add(first_result, second_result), prod1);
    }
}
