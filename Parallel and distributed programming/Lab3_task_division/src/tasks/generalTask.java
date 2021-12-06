package tasks;

import adt.Matrix;
import adt.Pair;
import entities.Utility;

import java.util.ArrayList;
import java.util.List;

public abstract class generalTask extends Thread {
    public int row_start;
    public int col_start;
    public Matrix first_matrix;
    public Matrix second_matrix;
    public Matrix final_matrix;
    public int task_size;
    public List<Pair<Integer, Integer>> pair_list;

    public generalTask(int row_start, int col_start, Matrix first_matrix, Matrix second_matrix, Matrix final_matrix, int task_size) {
        this.row_start = row_start;
        this.col_start = col_start;
        this.first_matrix = first_matrix;
        this.second_matrix = second_matrix;
        this.final_matrix = final_matrix;
        this.task_size = task_size;
        this.pair_list = new ArrayList<>();
        compute();
    }
    // declare a default case
    public generalTask() {
        this.row_start = 0;
        this.col_start = 0;
        this.first_matrix = new Matrix();
        this.second_matrix = new Matrix();
        this.final_matrix = new Matrix();
        this.task_size = 0;
        this.pair_list = new ArrayList<>();
    }

    @Override
    public void run() {
        for (Pair<Integer,Integer> pair: this.pair_list){
            try {
                this.final_matrix.setElement(pair.line, pair.column, Utility.generateElem(first_matrix, second_matrix, pair.line, pair.column));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public abstract void compute();

}
