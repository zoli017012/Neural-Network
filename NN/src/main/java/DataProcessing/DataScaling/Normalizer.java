package DataProcessing.DataScaling;
import Math.Matrix;

public class Normalizer {
    public static void standardNormalization(Matrix input){
        double[] means = getMean(input);
        double[] stds = getStandardDeviation(input);
        for (int i = 0; i < input.getColCount(); i++) {
            for (int j = 0; j < input.getRowCount(); j++) {
                double value = (input.get(j,i) - means[i]) / (stds[i] + 0.0000001);
                input.set(j,i,value);
            }
        }
    }
    private static double[] getMean(Matrix input){
        int row = input.getRowCount();
        int col = input.getColCount();
        double[] res = new double[input.getColCount()];
        for (int i = 0; i < col; i++) {
            double avg = 0;
            for (int j = 0; j < row; j++) {
                avg += input.get(j,i);
            }
            avg /= row;
            res[i] = avg;
        }
        return res;
    }
    private static double[] getStandardDeviation(Matrix input){
        int row = input.getRowCount();
        int col = input.getColCount();
        double[] res = new double[input.getColCount()];
        double[] means = getMean(input);
        for (int i = 0; i < col; i++) {
            double sum = 0;
            for (int j = 0; j < row; j++) {
                sum += Math.pow(input.get(j,i) - means[i],2);
            }
            sum /= row;
            sum = Math.sqrt(sum);
            res[i] = sum;
        }
        return res;
    }
}
