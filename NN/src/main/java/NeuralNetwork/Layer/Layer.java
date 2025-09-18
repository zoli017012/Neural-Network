package NeuralNetwork.Layer;
import Math.Matrix;
import NeuralNetwork.ActivationFunction.Activation;
import NeuralNetwork.ActivationFunction.ActivationFunction;

public class Layer {
    private Matrix weights;
    private double bias;
    Matrix activatedOutput;
    Matrix unactivatedOutput;
    int kernelSize, stride, filters;
    private ActivationFunction activationFunction = ActivationFunction.none;

    public Layer(int kernelSize, int stride, int filters){
        this.kernelSize = kernelSize;
        this.stride = stride;
        this.filters = filters;
        this.bias = 0;
    }

    public Layer() {

    }

    public Matrix getWeights() {
        return weights;
    }

    public void setWeights(Matrix weights) {
        this.weights = weights;
    }

    public Layer(int poolSize, int stride) {
        this.kernelSize = poolSize;
        this.stride = stride;
    }

    public Layer(int neuron, int connections, ActivationFunction activationFunction) {
        this.weights = Matrix.random(neuron, connections);
        this.bias = 0;
        this.activationFunction = activationFunction;
    }
    public Matrix activateLayer(Matrix input){
        if (activationFunction == ActivationFunction.ReLu) {
            return Activation.relu(unactivatedOutput);
        }
        else if (activationFunction == ActivationFunction.sigmoid) {
            return Activation.sigmoid(unactivatedOutput);
        }
        else if (activationFunction == ActivationFunction.softmax) {
            return Activation.softmax(unactivatedOutput);
        }
        return input;
    }
    public void forward(Matrix input){
        unactivatedOutput = (weights.dot(input)).add(bias);
        activatedOutput = activateLayer(unactivatedOutput);
    }

    public ActivationFunction getActivationFunction() {
        return activationFunction;
    }

    public boolean isActivated() {
        return activationFunction != ActivationFunction.none;
    }

    public Matrix getMatrix(){
        return this.weights;
    }

    public void updateWeights(Matrix matrix) {
        this.weights = matrix;
    }

    public double getBias() {
        return this.bias;
    }

    public void setBias(double value) {
        this.bias = value;
    }

    public Matrix getActivatedOutput() {
        return activatedOutput;
    }

    public void setActivatedOutput(Matrix activatedOutput) {
        this.activatedOutput = activatedOutput;
    }

    public Matrix getUnactivatedOutput() {
        return unactivatedOutput;
    }

    public void setUnactivatedOutput(Matrix unactivatedOutput) {
        this.unactivatedOutput = unactivatedOutput;
    }
}
