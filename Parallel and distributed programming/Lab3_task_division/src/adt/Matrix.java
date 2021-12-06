package adt;

import java.util.Random;
import java.util.Arrays;

public class Matrix {
    public int[][] matrix;
    public int rows;
    public int cols;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        this.matrix = new int[rows][cols];
    }

    public Matrix() {
        this.rows = 0;
        this.cols = 0;
    }

    public void randomMatrix() {
        for (int i = 0; i <= this.rows - 1; i++)
            for (int j = 0; j <= this.cols - 1; j++) {
                Random rand = new Random();
                this.matrix[i][j] = rand.nextInt(this.rows + this.cols);
            }
    }

    public int[][] getMatrix() {
        return this.matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public int getElement(int row, int col) {
        return this.matrix[row][col];
    }

    public void setElement(int row, int col, int element) {
        this.matrix[row][col] = element;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Matrix: \n");
        for (int i = 0; i <= rows - 1; i++) {
            for (int j = 0; j <= cols - 1; j++) {
                stringBuilder.append(this.matrix[i][j]);
                stringBuilder.append(" ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
