package NeuralNetwork.Model;
import Math.Matrix;

import NeuralNetwork.ActivationFunction.Activation;
import NeuralNetwork.ActivationFunction.ActivationFunction;
import NeuralNetwork.Layer.Dense;
import NeuralNetwork.Layer.Layer;

public abstract class Model {
    Layer[] layers;
    Matrix [] trainInput;
    Matrix [] trainOutput;
    Matrix validateInput;
    Matrix validateOutput;
    double learning_rate = 0.1;
    int batchSize;
    static int firstDenseLayer = 0;

    public Model(Layer[] layers, Matrix trainInput, Matrix trainOutput, Matrix validateInput, Matrix validateOutput, int batchSize) {
        while (!(layers[firstDenseLayer] instanceof Dense)) {
            firstDenseLayer++;
        }
        this.batchSize = batchSize;
        this.layers = layers;
        this.validateInput = validateInput;
        this.validateOutput = validateOutput;
        Matrix[] trainInputBatches = createBatches(trainInput);
        Matrix[] trainOutputBatches = createBatches(trainOutput);
        this.trainInput = trainInputBatches;
        this.trainOutput = trainOutputBatches;
    }

    public Model(Layer[] layers) {
        this.layers = layers;
    }

    public Layer[] getLayers() {
        return layers;
    }

    public void setLayers(Layer[] layers) {
        this.layers = layers;
    }

    public Matrix[] createBatches(Matrix matrix){
        int numberOfBatches = matrix.getColCount() / batchSize;
        Matrix[] batches = new Matrix[numberOfBatches];
        int left = 0;
        int right = batchSize;
        for (int i = 0; i < numberOfBatches; i++) {
            Matrix subMatrix = matrix.submatrix(left,right);
            batches[i] = subMatrix;
            left = right;
            right += batchSize;
        }
        return batches;
    }
    public NetworkOutput feedForward(Matrix input) {
        NetworkOutput networkOutput = new NetworkOutput(layers.length);
        layers[firstDenseLayer].forward(input);
        networkOutput.addActivatedOutput(layers[firstDenseLayer].getActivatedOutput());
        networkOutput.addUnactivatedOutput(layers[firstDenseLayer].getUnactivatedOutput());
        for (int i = firstDenseLayer+1; i < layers.length; i++) {
            if (layers[i-1].isActivated()) {
                layers[i].forward(layers[i-1].getActivatedOutput());
            }
            else{
                layers[i].forward(layers[i-1].getUnactivatedOutput());
            }
            networkOutput.addActivatedOutput(layers[i].getActivatedOutput());
            networkOutput.addUnactivatedOutput(layers[i].getUnactivatedOutput());
        }
        return networkOutput;
    }
    public NetworkDifferences backpropagation(Matrix input, NetworkOutput networkOutputs, Matrix actualOutput){
        NetworkDifferences networkDifferences = new NetworkDifferences(layers.length);
        Matrix[] unactivatedOutputs = networkOutputs.getUnactivatedOutputs();
        Matrix[] activatedOutputs = networkOutputs.getActivatedOutputs();

        int pointer = 0;
        int numberOfSamples = input.getColCount();
        float matrixScalar = (float) 1 / numberOfSamples;
        Matrix unactivatedLayerDifference;

        if (activatedOutputs[activatedOutputs.length-1] != null) {
            unactivatedLayerDifference = (activatedOutputs[activatedOutputs.length-1].subtract(actualOutput));
        }
        else{
            unactivatedLayerDifference = (unactivatedOutputs[unactivatedOutputs.length-1].subtract(actualOutput));
        }

        double biasDifference = unactivatedLayerDifference.sum() * matrixScalar;
        networkDifferences.addBiasDifference(biasDifference);
        Matrix weightDifference;

        for (int i = unactivatedOutputs.length-2; i >= firstDenseLayer; i--) {
            if (activatedOutputs[i] != null) {
                weightDifference = (
                        unactivatedLayerDifference
                        .dot(activatedOutputs[i].transpose()))
                        .scalar(matrixScalar);
            }
            else {
                weightDifference = (
                        unactivatedLayerDifference
                        .dot(unactivatedOutputs[i].transpose()))
                        .scalar(matrixScalar);
            }
            networkDifferences.addWeightDifference(weightDifference);
            unactivatedLayerDifference = (
                    layers[layers.length - 1 - pointer].getMatrix()
                            .transpose())
                            .dot(unactivatedLayerDifference);
            if (layers[layers.length - 2 - pointer].isActivated()) {
                if (layers[layers.length-2-pointer].getActivationFunction() == ActivationFunction.ReLu) {
                    unactivatedLayerDifference = unactivatedLayerDifference.multiply(Activation.reluDerivative(unactivatedOutputs[i]));
                }
                else if (layers[layers.length-2-pointer].getActivationFunction() == ActivationFunction.sigmoid) {
                    unactivatedLayerDifference = unactivatedLayerDifference.multiply(Activation.sigmoidDerivative(unactivatedOutputs[i]));
                }
            }
            biasDifference = unactivatedLayerDifference.sum() * matrixScalar;
            pointer++;
            networkDifferences.addBiasDifference(biasDifference);
        }
        weightDifference = (
                unactivatedLayerDifference
                        .dot(input.transpose()))
                        .scalar(matrixScalar);
        networkDifferences.addWeightDifference(weightDifference);
        return networkDifferences;
    }
    public void updateParameters(NetworkDifferences differences){
        Matrix[] weightDifferences = differences.getWeightDifferences();
        double[] biasDifferences = differences.getBiasDifferences();

        for (int i = firstDenseLayer; i < layers.length; i++) {
            Matrix weightDifference = weightDifferences[weightDifferences.length-1-i];
            double biasDifference = biasDifferences[biasDifferences.length-1-i];

            layers[i].updateWeights(layers[i].getMatrix().subtract(weightDifference.scalar(learning_rate)));
            layers[i].setBias(layers[i].getBias() - (biasDifference*learning_rate));
        }
    }
    public Matrix predict(Matrix input) {
        NetworkOutput outputs = feedForward(input);
        Matrix[] activatedOutputs = outputs.getActivatedOutputs();
        Matrix[] unactivatedOutputs = outputs.getUnactivatedOutputs();
        int lastLayer = layers.length-1;

        if (activatedOutputs[lastLayer] != null) {
            return activatedOutputs[lastLayer];
        }
        return unactivatedOutputs[lastLayer];
    }
    public double getAccuracy(Matrix prediction, Matrix actualOutput){
        int accuracy = 0;
        for (int i = 0; i < prediction.getColCount(); i++) {
            int max = 0;
            for (int j = 1; j < prediction.getRowCount(); j++) {
                if (prediction.get(j,i) > prediction.get(max,i)) {
                    max = j;
                }
            }
            if (actualOutput.get(max,i) == 1) {
                accuracy++;
            }
        }
        return (accuracy / (float) prediction.getColCount()) * 100;
    }
    public double meanSquaredLoss(Matrix prediction, Matrix actualOutput){
        int samples = prediction.getColCount();
        Matrix difference = prediction.subtract(actualOutput);
        Matrix squaredDifference = difference.power(2);
        double loss = squaredDifference.sum();

        return loss / samples;
    }
    public void train(int epoch) {
        for (int i = 0; i <= epoch; i++) {
            for (int j = 0; j < trainInput.length; j++) {
                Matrix batchInput = trainInput[j];
                Matrix batchOutput = trainOutput[j];

                NetworkOutput outputs = feedForward(batchInput);
                NetworkDifferences networkDifferences = backpropagation(batchInput, outputs, batchOutput);
                updateParameters(networkDifferences);
            }
            Matrix prediction = predict(validateInput);
            double accuracy = getAccuracy(prediction, validateOutput);
            double loss = meanSquaredLoss(prediction, validateOutput);
            System.out.printf("%d. epoch: %.4f loss %.1f%% validation accuracy\n",i,loss,accuracy);
        }
    }
    public void compile(Matrix testX, Matrix testY) {
        Matrix prediction = predict(testX);
        if (this instanceof ClassificationModel) {
            testY = Matrix.oneHotEncode(testY);
        }
        double accuracy = getAccuracy(prediction, testY);
        double loss = meanSquaredLoss(prediction, testY);
        System.out.printf("%.4f loss %.1f%% test accuracy\n",loss,accuracy);
    }
    public static class NetworkOutput {
        Matrix[] unactivatedOutputs;
        Matrix[] activatedOutputs;
        int activatedOutputsPointer = firstDenseLayer;
        int unactivatedOutputsPointer = firstDenseLayer;

        public NetworkOutput(int numberOfLayers) {
            unactivatedOutputs = new Matrix[numberOfLayers];
            activatedOutputs = new Matrix[numberOfLayers];
        }

        public void addActivatedOutput(Matrix output){
            activatedOutputs[activatedOutputsPointer] = output;
            activatedOutputsPointer++;
        }
        public void addUnactivatedOutput(Matrix output){
            unactivatedOutputs[unactivatedOutputsPointer] = output;
            unactivatedOutputsPointer++;
        }
        public Matrix getNetworkOutput(){
            if (activatedOutputs[activatedOutputs.length-1] != null) {
                return activatedOutputs[activatedOutputs.length-1];
            }
            return unactivatedOutputs[unactivatedOutputs.length-1];
        }

        public Matrix[] getUnactivatedOutputs(){
            return unactivatedOutputs;
        }

        public Matrix[] getActivatedOutputs(){
            return activatedOutputs;
        }
    }
    private static class NetworkDifferences {
        Matrix[] weightDifferences;
        double [] biasDifferences;
        int biasDifferencesPointer = 0;
        int weightDifferencesPointer = 0;

        public NetworkDifferences(int numberOfLayers) {
            weightDifferences = new Matrix[numberOfLayers];
            biasDifferences = new double[numberOfLayers];
        }

        public void addBiasDifference(double output){
            biasDifferences[biasDifferencesPointer] = output;
            biasDifferencesPointer++;
        }
        public void addWeightDifference(Matrix output){
            weightDifferences[weightDifferencesPointer] = output;
            weightDifferencesPointer++;
        }

        public Matrix[] getWeightDifferences(){
            return weightDifferences;
        }

        public double[] getBiasDifferences(){
            return biasDifferences;
        }
    }

}
