import adt.Matrix;
import entities.runningProperies;
import operations.NormalApproach;
import operations.ThreadPoolApproach;

import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        boolean generateTests = false;
        runningProperies properties = new runningProperies();

        if(!generateTests) {
            Matrix first_matrix = new Matrix(properties.FIRST_SIZE, properties.SECOND_SIZE);
            Matrix second_matrix = new Matrix(properties.SECOND_SIZE, properties.FIRST_SIZE);
            first_matrix.randomMatrix();
            second_matrix.randomMatrix();
            Matrix final_matrix = new Matrix(properties.FIRST_SIZE, properties.FIRST_SIZE);

            System.out.println(first_matrix);
            System.out.println(second_matrix);

            float start_time = System.nanoTime();

            if (properties.APPROACH.equals("NORMAL"))
                NormalApproach.run(first_matrix, second_matrix, properties.THREAD_NB, properties.TYPE, final_matrix);
            else
                ThreadPoolApproach.run(first_matrix, second_matrix, properties.THREAD_NB, properties.TYPE, final_matrix);

            System.out.println(final_matrix);
            System.out.println((System.nanoTime() - start_time) / 100000000);
        }
        else {
            FileWriter writer = new FileWriter("out.txt");
            for(String approach_type : properties.APPROACH_LIST) {
                System.out.println(approach_type);
                for(String type: properties.TYPE_LIST) {
                    System.out.println(type);
                    for(int thread_nb : properties.THREAD_LIST) {
                        System.out.println(thread_nb);
                        for(int matrix_size : properties.FIRST_SIZE_LIST) {

                            Matrix first_matrix = new Matrix(matrix_size, matrix_size);
                            Matrix second_matrix = new Matrix(matrix_size, matrix_size);
                            Matrix final_matrix = new Matrix(matrix_size, matrix_size);
                            first_matrix.randomMatrix();
                            second_matrix.randomMatrix();
                            float start_time = System.nanoTime();
                            if (approach_type.equals("NORMAL"))
                                NormalApproach.run(first_matrix, second_matrix, thread_nb, type, final_matrix);
                            else
                                ThreadPoolApproach.run(first_matrix, second_matrix, thread_nb, type, final_matrix);
                            float end_time = (System.nanoTime() - start_time) / 100000000;
                            // write to output file
                            writer.write(approach_type + " | " + type + " | " + "Threads: " + thread_nb + " | " +
                                    "Size: " + matrix_size + " -> Time:" + end_time + "\n");
                        }
                    }
                }
            }
            writer.close();
        }
    }
}
