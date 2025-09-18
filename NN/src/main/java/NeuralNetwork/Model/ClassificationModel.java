package NeuralNetwork.Model;

import DataProcessing.DataScaling.Normalizer;
import NeuralNetwork.ActivationFunction.ActivationFunction;
import NeuralNetwork.Layer.Layer;
import Math.Matrix;

public class ClassificationModel extends Model{
    public ClassificationModel(Layer[] layers, Matrix trainInput, Matrix trainOutput, Matrix validateInput, Matrix validateOutput, int batchSize) {
        super(layers, trainInput, Matrix.oneHotEncode(trainOutput), validateInput, Matrix.oneHotEncode(validateOutput), batchSize);
    }
    public ClassificationModel(Layer[] layers, Matrix trainInput, Matrix trainOutput, Matrix validateInput, Matrix validateOutput) {
        super(layers, trainInput, Matrix.oneHotEncode(trainOutput), validateInput, Matrix.oneHotEncode(validateOutput), trainInput.getColCount());
    }

    public ClassificationModel(Layer[] layers) {
        super(layers);
    }
}
