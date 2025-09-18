package Math;

import java.util.Arrays;

public class Matrix {
    private double [][] matrix;


    public Matrix(int row, int col) {
        this.matrix = new double[row][col];
    }
    public Matrix(double[][] matrix) {
        this.matrix = matrix;
    }
    public String getShape(){
        return "("+matrix.length+","+matrix[0].length+")";
    }
    public int getRowCount(){
        return matrix.length;
    }
    public int getColCount(){
        return matrix[0].length;
    }
    public double get(int i, int j){
        return this.matrix[i][j];
    }
    public void set(int i, int j, double value){
        this.matrix[i][j] = value;
    }
    public Matrix power(int power){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = Math.pow(matrix[i][j], power);
            }
        }
        return this;
    }
    public Matrix submatrix(int left, int right){
        Matrix submatrix = new Matrix(this.matrix.length, (right - left + 1));
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = left; j < right; j++) {
                submatrix.matrix[i][j-left] = this.get(i,j);
            }
        }
        return submatrix;
    }
    public static Matrix random(int row, int col){
        double lower = -1/Math.sqrt(col);
        double upper = 1/Math.sqrt(col);
        Matrix res = new Matrix(row, col);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                double value = Math.random();
                value = lower + value * (upper- lower);
                res.matrix[i][j] = value;
            }
        }
        return res;
    }
    public static Matrix randomInt(int low, int high, int row, int col){
        Matrix res = new Matrix(row, col);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int rnd = (int)(Math.random()*(high-low)+low);
                res.matrix[i][j] = rnd;
            }
        }
        return res;
    }
    public void print(){
        System.out.print("[");
        for (int i = 0; i < this.matrix.length-1; i++) {
            System.out.println(Arrays.toString(matrix[i]));
        }
        System.out.print(Arrays.toString(matrix[this.matrix.length-1]));
        System.out.println("]");
    }
    public Matrix dot(Matrix other){
        int row = this.matrix.length;
        int col = other.matrix[0].length;
        Matrix res = new Matrix(row, col);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                double s = 0;
                for (int k = 0; k < this.matrix[0].length; k++) {
                    s+=this.matrix[i][k]*other.matrix[k][j];
                }
                res.matrix[i][j] = s;
            }
        }
        return res;
    }
    public Matrix multiply(Matrix matrix){
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix[0].length; j++) {
                this.matrix[i][j] *= matrix.get(i,j);
            }
        }
        return this;
    }
    public Matrix transpose(){
        int row = this.matrix[0].length;
        int col = this.matrix.length;
        Matrix res = new Matrix(row, col);
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                res.matrix[j][i] = this.matrix[i][j];
            }
        }
        return res;
    }
    public Matrix add(Matrix other){
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix[0].length; j++) {
                this.matrix[i][j] += other.matrix[i][j];
            }
        }
        return this;
    }
    public Matrix subtract(Matrix other){
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix[0].length; j++) {
                this.matrix[i][j] -= other.matrix[i][j];
            }
        }
        return this;
    }
    public Matrix add(double n){
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix[0].length; j++) {
                this.matrix[i][j] += n;
            }
        }
        return this;
    }
    public double sum(){
        double s = 0;
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix[0].length; j++) {
                s += this.matrix[i][j];
            }
        }
        double [][] res = {{s}};
        return s;
    }
    public Matrix scalar(double n){
        for (int i = 0; i < this.matrix.length; i++) {
            for (int j = 0; j < this.matrix[0].length; j++) {
                this.matrix[i][j] *= n;
            }
        }
        return this;
    }
    public static Matrix oneHotEncode(Matrix matrix){
        int max = 0;
        for (int i = 0; i < matrix.getColCount(); i++) {
            if (matrix.get(0,i) > max) {
                max = (int)matrix.get(0,i);
            }
        }
        Matrix res = new Matrix(max+1, matrix.getColCount());
        for (int i = 0; i < res.getColCount(); i++) {
            int out = (int)matrix.get(0,i);
            res.set(out,i,1);
        }
        return res;
    }
    public Matrix getRow(int index){
        Matrix row = new Matrix(1, this.getColCount());
        for (int i = 0; i < this.getColCount(); i++) {
            row.set(0,i,this.get(index,i));
        }
        return row;
    }
    public Matrix getColumn(int index){
        Matrix column = new Matrix(this.getRowCount(),1);
        for (int i = 0; i < this.getRowCount(); i++) {
            column.set(i,0,this.get(i,index));
        }
        return column;
    }
    public Matrix reshape(int width, int height){
        Matrix newMatrix = new Matrix(width,height);
        int pointer = 0;
        for (int j = 0; j < width; j++) {
            for (int k = 0; k < height; k++) {
                newMatrix.set(j,k,this.get(pointer,0));
                pointer++;
            }
        }
        return newMatrix;
    }
    public static Matrix padding(Matrix input, int kernelSize){
        int padding = 0;
        int size = input.getColCount();
        while ((size+padding*2) % kernelSize != 0) {
            padding++;
        }
        Matrix pad = new Matrix(size+(padding*2), size+(padding*2));
        for (int i = padding; i < size; i++) {
            for (int j = padding; j < size; j++) {
                pad.set(i,j, input.get(i-padding,j-padding));
            }
        }
        return pad;
    }
    public void setColumn(Matrix column, int index){
        for (int i = 0; i < this.getRowCount(); i++) {
            this.set(i,index, column.get(i,0));
        }
    }
    public void setRow(Matrix row, int index){
        for (int i = 0; i < this.getColCount(); i++) {
            this.set(index,i, row.get(0,i));
        }
    }
    public int argmax(){
        if (this.getColCount() != 1) {
            //TODO: Throw exception
            return -1;
        }
        int max = 0;
        for (int i = 1; i < this.getRowCount(); i++) {
            if (this.get(i,0) > this.get(max,0)) {
                max = i;
            }
        }
        return max;
    }
}
