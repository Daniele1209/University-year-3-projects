import Entities.Operation;
import Entities.Polynomial;

import java.util.concurrent.ExecutionException;

public class Program {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Declare the two polynomial
        Polynomial first_polynomial = new Polynomial(50);
        System.out.println("first_polynomial:" + first_polynomial);
        Polynomial second_polynomial = new Polynomial(50);
        System.out.println("second_polynomial:" + second_polynomial);

        // Simple Sequential
        System.out.println(simpleSequential(first_polynomial, second_polynomial).toString()+"\n");
        // Simple Threaded
        System.out.println(simpleThreaded(first_polynomial, second_polynomial).toString()+"\n");
        //Karatsuba Sequential
        System.out.println(karatsubaSequential(first_polynomial, second_polynomial).toString()+"\n");
        //Karatsuba Threaded
        System.out.println(karatsubaThreaded(first_polynomial, second_polynomial).toString()+"\n");
    }

    // Run simple sequential
    private static Polynomial simpleSequential(Polynomial pol1, Polynomial pol2) {
        long start = System.currentTimeMillis();
        Polynomial res = Operation.simpleSequential(pol1, pol2);
        System.out.println("Simple sequential: ");
        System.out.println("Done in: " + (System.currentTimeMillis() - start) + " ms");
        return res;
    }
    // Run simple threaded
    private static Polynomial simpleThreaded(Polynomial pol1, Polynomial pol2) throws InterruptedException {
        long start = System.currentTimeMillis();
        Polynomial res = Operation.simpleThreaded(pol1, pol2);
        System.out.println("Simple threaded: ");
        System.out.println("Done in: " + (System.currentTimeMillis() - start) + " ms");
        return res;
    }
    // Run karatsuba sequential
    private static Polynomial karatsubaSequential(Polynomial pol1, Polynomial pol2) {
        long start = System.currentTimeMillis();
        Polynomial res = Operation.karatsubaSequential(pol1, pol2);
        System.out.println("Karatsuba sequential: ");
        System.out.println("Done in: " + (System.currentTimeMillis() - start) + " ms");
        return res;
    }
    // Run karatsuba threaded
    private static Polynomial karatsubaThreaded(Polynomial pol1, Polynomial pol2) throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        Polynomial res = Operation.karatsubaThreaded(pol1, pol2);
        System.out.println("Karatsuba threaded: ");
        System.out.println("Done in: " + (System.currentTimeMillis() - start) + " ms");
        return res;
    }
}
