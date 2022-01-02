package entities;

import adt.Matrix;
import tasks.TaskCol;
import tasks.TaskKth;
import tasks.TaskRow;
import tasks.generalTask;

public class Utility {

    public static generalTask threadInitialization(int idx, Matrix first_matrix, Matrix second_matrix, Matrix final_matrix, int thread_nb, String task) {
        // task using rows (1)
        if(task.equals("task-row")) {
            int final_size = final_matrix.rows * final_matrix.cols;
            int count = final_size / thread_nb;

            int row_start = count * idx / final_matrix.rows;
            int col_start = count * idx % final_matrix.rows;
            if (idx == thread_nb - 1)
                count += final_size % thread_nb;

            return new TaskRow(row_start, col_start, first_matrix, second_matrix, final_matrix, count);
        }
        // task using columns (2)
        else if(task.equals("task-col")) {
            int final_size = final_matrix.rows * final_matrix.cols;
            int count = final_size / thread_nb;

            int row_start = count * idx % final_matrix.rows;
            int col_start = count * idx / final_matrix.rows;
            if (idx == thread_nb - 1)
                count += final_size % thread_nb;

            return new TaskCol(row_start, col_start, first_matrix, second_matrix, final_matrix, count);
        }
        // task using k-th element (3)
        else {
            int final_size = final_matrix.rows * final_matrix.cols;
            int count = final_size / thread_nb;

            if (idx < final_size % thread_nb)
                count++;
            int row_start = idx / final_matrix.cols;
            int col_start = idx % final_matrix.cols;
            return new TaskKth(row_start, col_start, first_matrix, second_matrix, final_matrix, count);
        }
    }

    public static int generateElem(Matrix first_matrix, Matrix second_matrix, int row, int col) throws Exception {
        int generated_element = 0;
        if(first_matrix.rows > row && second_matrix.cols > col) {

            for (int idx = 0; idx < first_matrix.cols; idx++) {
                generated_element += first_matrix.getElement(row, idx) * second_matrix.getElement(idx, col);
            }
        }
        return generated_element;
    }
}
