package tasks;

import adt.Matrix;
import adt.Pair;

public class TaskCol extends  generalTask {

    public TaskCol(int row_start, int col_start, Matrix first_matrix, Matrix second_matrix, Matrix final_matrix, int task_size) {
        super(row_start, col_start, first_matrix, second_matrix, final_matrix, task_size);
    }

    @Override
    public void compute() {
        int row = row_start;
        int col = col_start;
        int current_size = task_size;
        while (row < final_matrix.rows && col < final_matrix.cols && current_size != 0) {
            pair_list.add(new Pair<>(row, col));
            row++;
            if (row == final_matrix.cols) {
                row = 0;
                col++;
            }
            current_size--;
        }
    }

}
