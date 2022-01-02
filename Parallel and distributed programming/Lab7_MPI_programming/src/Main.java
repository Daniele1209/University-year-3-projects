import Entities.Operation;
import Entities.Polynomial;

import mpi.MPI;

public class Main {

    private static void multiplicationMaster(Polynomial pol1, Polynomial pol2, int size, Boolean isSimple) {
        long start = System.currentTimeMillis();
        int begin = 0;
        int end = 0;
        int length = pol1.getLength() / (size - 1);

        for (int idx = 1; idx < size; idx++) {
            begin = end;
            end += length;

            if (idx == size - 1) {
                begin = pol1.getLength();
            }

            MPI.COMM_WORLD.Send(new Object[]{pol1}, 0, 1, MPI.OBJECT, idx, 0);
            MPI.COMM_WORLD.Send(new Object[]{pol2}, 0, 1, MPI.OBJECT, idx, 0);
            MPI.COMM_WORLD.Send(new int[]{begin}, 0, 1, MPI.INT, idx, 0);
            MPI.COMM_WORLD.Send(new int[]{end}, 0, 1, MPI.INT, idx, 0);

        }

        Object[] results = new Object[size - 1];
        for (int idx = 1; idx < size; idx++) {
            MPI.COMM_WORLD.Recv(results, idx - 1, 1, MPI.OBJECT, idx, 0);
        }

        Polynomial result = Operation.buildResult(results);
        if(isSimple)
            System.out.println("Simple type multiplication ->\n" + result.toString());
        else
            System.out.println("Karatsuba type multiplication ->\n" + result.toString());
        System.out.println("Time -> " + (System.currentTimeMillis() - start) + " ms");
    }

    private static void multiplication_SimpleWorker(int rank) {
        System.out.printf("-> worker nb %d start\n", rank);

        Object[] pol1 = new Object[2];
        Object[] pol2 = new Object[2];
        int[] begin = new int[1];
        int[] end = new int[1];

        MPI.COMM_WORLD.Recv(pol1, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(pol2, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(begin, 0, 1, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Recv(end, 0, 1, MPI.INT, 0, 0);
        Polynomial result = Operation.multiplicationOperation(pol1[0], pol2[0], begin[0], end[0]);

        MPI.COMM_WORLD.Send(new Object[]{result}, 0, 1, MPI.OBJECT, 0, 0);

    }

    private static void multiplication_KaratsubaWorker(int rank) {
        System.out.printf("Worker %d started\n", rank);

        Object[] pol1 = new Object[2];
        Object[] pol2 = new Object[2];
        int[] begin = new int[1];
        int[] end = new int[1];

        MPI.COMM_WORLD.Recv(pol1, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(pol2, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(begin, 0, 1, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Recv(end, 0, 1, MPI.INT, 0, 0);

        Polynomial polynomial1 = (Polynomial) pol1[0];
        Polynomial polynomial2 = (Polynomial) pol2[0];

        // Setting the coefficients of each polynomial
        for (int idx = 0; idx < begin[0]; idx++) {
            polynomial1.getCoefficients().set(idx, 0);
        }
        for (int idx = end[0]; idx < polynomial2.getCoefficients().size(); idx++) {
            polynomial2.getCoefficients().set(idx, 0);

        }

        Polynomial result = Operation.karatsubaThreaded(polynomial1, polynomial2);
        MPI.COMM_WORLD.Send(new Object[]{result}, 0, 1, MPI.OBJECT, 0, 0);
    }

    public static void main(String[] args) {
        Boolean isSimple = false;
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        // Check for master process
        if (rank == 0) {

            System.out.println("Generating polynomials:");
            Polynomial polynomial1 = new Polynomial(5);
            Polynomial polynomial2 = new Polynomial(5);
            System.out.println(polynomial1);
            System.out.println(polynomial2);

            multiplicationMaster(polynomial1, polynomial2, size, isSimple);
        } else {
            if(isSimple)
                multiplication_SimpleWorker(rank);
            else
                multiplication_KaratsubaWorker(rank);
        }
        MPI.Finalize();
    }


}
