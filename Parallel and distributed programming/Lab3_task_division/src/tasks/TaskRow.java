package tasks;

import adt.Matrix;
import adt.Pair;

public class TaskRow extends generalTask {

    public TaskRow(int row_start, int col_start, Matrix first_matrix, Matrix second_matrix, Matrix final_matrix, int task_size) {
        super(row_start, col_start, first_matrix, second_matrix, final_matrix, task_size);
    }

    public TaskRow() {
        super();
    }

    @Override
    public void compute() {
        int row = row_start;
        int col = col_start;
        int current_size = task_size;
        while (row < final_matrix.rows && col < final_matrix.cols && current_size > 0) {
            pair_list.add(new Pair<>(row, col));
            col++;
            if (col == final_matrix.rows) {
                col = 0;
                row++;
            }
            current_size--;
        }
    }
}
