package DataProcessing;
import DataProcessing.Exceptions.DataSplitException;
import Math.Matrix;

public class TrainTestValidationSplit {
    public Matrix trainX;
    public Matrix trainY;
    public Matrix testX;
    public Matrix testY;
    public Matrix validationX;
    public Matrix validationY;



    public TrainTestValidationSplit(Matrix input, Matrix output, double trainSize, double testSize) throws DataSplitException {

        if ( (trainSize + testSize) > 1.0 ) {
            //TODO: Lekezelni, hogy ha többet írnak be
        }
        int samples = input.getColCount();
        int lengthOfSamples = input.getRowCount();

        int trainSamples = (int)(samples*trainSize);
        Matrix trainX = new Matrix(lengthOfSamples, trainSamples);
        Matrix trainY = new Matrix(1, trainSamples);
        for (int i = 0; i < lengthOfSamples; i++) {
            for (int j = 0; j < trainSamples; j++) {
                trainX.set(i,j, input.get(i,j));
                trainY.set(0,j, output.get(0,j));
            }
        }
        int testSamples = (int)(samples*testSize);
        Matrix testX = new Matrix(lengthOfSamples, testSamples);
        Matrix testY = new Matrix(1, testSamples);
        for (int i = 0; i < lengthOfSamples; i++) {
            for (int j = 0; j < testSamples; j++) {
                testX.set(i,j, input.get(i,j+trainSamples));
                testY.set(0,j, output.get(0,j+trainSamples));
            }
        }

        int validationSamples = samples - (trainSamples + testSamples);
        Matrix validationX = new Matrix(lengthOfSamples, validationSamples);
        Matrix validationY = new Matrix(1, validationSamples);
        for (int i = 0; i < lengthOfSamples; i++) {
            for (int j = 0; j < (samples - (trainSamples + testSamples)); j++) {
                validationX.set(i,j, input.get(i,j+(testSamples + trainSamples)));
                validationY.set(0,j, output.get(0,j+(testSamples + trainSamples)));
            }
        }
        this.trainX = trainX;
        this.trainY = trainY;
        this.testX = testX;
        this.testY = testY;
        this.validationX = validationX;
        this.validationY = validationY;
    }
}
