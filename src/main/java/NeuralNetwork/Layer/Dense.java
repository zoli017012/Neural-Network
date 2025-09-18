package NeuralNetwork.Layer;
import Math.Matrix;
import NeuralNetwork.ActivationFunction.ActivationFunction;

public class Dense extends Layer{

    public Dense(int neuron, int connections) {
        super(neuron, connections);
    }

    public Dense(int neuron, int connections, ActivationFunction activationFunction) {
        super(neuron, connections, activationFunction);
    }
}
