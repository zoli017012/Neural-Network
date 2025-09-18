package NeuroEvolution.Individuals;

import NeuralNetwork.Model.Model;
import Math.Matrix;
import NeuroEvolution.Entities.FrameEntity;

public abstract class Individual implements IndividualInterface{
    private Model brain;
    FrameEntity frameEntity;
    private int score = 0;
    private static final double MUTATION_RATE = 0.001;

    public Individual(FrameEntity frameEntity, Model brain) {
        this.brain = brain;
        this.frameEntity = frameEntity;
    }

    public void setBrain(Model brain) {
        this.brain = brain;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Model getBrain() {
        return brain;
    }

    public FrameEntity getEntity() {
        return frameEntity;
    }


    public Matrix makeDecision(Matrix input){
        Model.NetworkOutput outputs = brain.feedForward(input);
        return outputs.getNetworkOutput();
    }

    public void mutate(){
        double mutationChance = Math.random();
        if (mutationChance <= MUTATION_RATE) {
            int randomLayerPointer = (int)(Math.random()*brain.getLayers().length);
            Matrix randomLayer = brain.getLayers()[randomLayerPointer].getMatrix();

            int randomNeuronRowPointer = (int)(Math.random()*randomLayer.getRowCount());
            int randomNeuronColumnPointer = (int)(Math.random()*randomLayer.getColCount());

            double mutatedValue = randomLayer.get(randomNeuronRowPointer, randomNeuronColumnPointer) + ((Math.random()-0.5));
            randomLayer.set(randomNeuronRowPointer,randomNeuronColumnPointer,mutatedValue);
        }
    }

    public void addScore(int score){
        this.score += score;
    }

}
