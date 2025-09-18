package NeuroEvolution.Population;

import NeuralNetwork.ActivationFunction.Activation;
import NeuroEvolution.Entities.FrameEntity;
import NeuroEvolution.Individuals.Ant;
import NeuroEvolution.Individuals.Individual;
import Math.Matrix;

public class AntPopulation extends Population{
    public AntPopulation(FrameEntity[] entities) {
        super(entities);
    }

    @Override
    public void generatePopulation() {
        for (int i = 0; i < population.length; i++) {
            population[i] = new Ant(frameEntities[i]);
        }
    }

    @Override
    public void selection() {
        sortByFitness();
        Individual[] newPopulation = new Individual[population.length];
        Matrix scoreOfIndividuals = new Matrix(population.length, 1);
        for (int i = 0; i < population.length; i++) {
            scoreOfIndividuals.set(i,0,population[i].getScore());
        }
        scoreOfIndividuals = Activation.softmax(scoreOfIndividuals);
        double[] chancesForReproduction = new double[population.length];
        chancesForReproduction[0] = scoreOfIndividuals.get(0,0);
        for (int i = 1; i < population.length; i++) {
            chancesForReproduction[i] = chancesForReproduction[i-1]+scoreOfIndividuals.get(i,0);
        }
        for (int i = 0; i < NUMBER_OF_ELITE_INDIVIDUALS; i++) {
            newPopulation[i] = population[i];
            newPopulation[i].setScore(1);
            newPopulation[i].getEntity().randomPosition();
        }
        for (int i = NUMBER_OF_ELITE_INDIVIDUALS; i < population.length; i++) {
            double parent1Choice = Math.random();
            double parent2Choice = Math.random();
            int parent1Pointer = 0;
            int parent2Pointer = 0;
            for (int j = 0; j < chancesForReproduction.length; j++) {
                if (parent1Choice <= chancesForReproduction[i]) {
                    parent1Pointer = i;
                    break;
                }
            }
            for (int j = 0; j < chancesForReproduction.length; j++) {
                if (parent2Choice <= chancesForReproduction[i]) {
                    parent2Pointer = i;
                    break;
                }
            }

            Individual parent1 = population[parent1Pointer];
            Individual parent2 = population[parent2Pointer];
            newPopulation[i] = crossover(parent1, parent2, i);
            newPopulation[i].getEntity().randomPosition();
        }
        population = newPopulation;
    }
}
