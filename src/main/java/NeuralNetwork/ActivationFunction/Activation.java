package NeuralNetwork.ActivationFunction;
import DataProcessing.DataScaling.Normalizer;
import Math.Matrix;

public abstract class Activation {
    public static Matrix relu(Matrix matrix){
        Normalizer.standardNormalization(matrix);
        for (int i = 0; i < matrix.getRowCount(); i++) {
            for (int j = 0; j < matrix.getColCount(); j++) {
                if (matrix.get(i,j) < 0) {
                    matrix.set(i,j,0);
                }
            }
        }

        return matrix;
    }
    public static Matrix reluDerivative(Matrix matrix){
        Matrix res = new Matrix(matrix.getRowCount(), matrix.getColCount());
        for (int i = 0; i < res.getRowCount(); i++) {
            for (int j = 0; j < res.getColCount(); j++) {
                if (matrix.get(i,j) <= 0){
                    res.set(i,j,0);
                }
                else{
                    res.set(i,j,1);
                }
            }
        }
        return res;
    }
    public static Matrix softmax(Matrix matrix){
        Matrix res = new Matrix(matrix.getRowCount(), matrix.getColCount());

        for (int i = 0; i < matrix.getRowCount(); i++) {
            for (int j = 0; j < matrix.getColCount(); j++) {
                double sum = 0;
                for (int k = 0; k < matrix.getRowCount(); k++) {
                    sum += Math.exp(matrix.get(k,j));
                }
                double newValue = Math.exp(matrix.get(i,j)) / sum;
                res.set(i,j,newValue);
            }
        }

        return res;
    }
    public static Matrix sigmoid(Matrix matrix){
        for (int i = 0; i < matrix.getRowCount(); i++) {
            for (int j = 0; j < matrix.getColCount(); j++) {
                double newValue = 1/(1+Math.exp(-matrix.get(i,j)));
                matrix.set(i,j,newValue);
            }
        }
        Normalizer.standardNormalization(matrix);
        return matrix;
    }
    public static Matrix sigmoidDerivative(Matrix matrix){
        Matrix res = new Matrix(matrix.getRowCount(), matrix.getColCount());
        for (int i = 0; i < matrix.getRowCount(); i++) {
            for (int j = 0; j < matrix.getColCount(); j++) {
                double newValue = Math.exp(matrix.get(i,j)) / (Math.pow((Math.exp(matrix.get(i,j)) + 1), 2));
                res.set(i,j,newValue);
            }
        }
        return res;
    }

}