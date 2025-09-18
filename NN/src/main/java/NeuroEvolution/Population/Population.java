package NeuroEvolution.Population;

import NeuroEvolution.Entities.FrameEntity;
import NeuroEvolution.Individuals.Ant;
import NeuroEvolution.Individuals.Individual;

public abstract class Population implements PopulationInterface{
    public static Individual[] population;
    protected static FrameEntity[] frameEntities;
    public static final int POPULATION_SIZE = 300;
    public static final int NUMBER_OF_ELITE_INDIVIDUALS = POPULATION_SIZE / 20;
    public Individual[] getPopulation() {
        return population;
    }
    public static FrameEntity[] getFrameEntities(){
        return frameEntities;
    }
    public Population(FrameEntity[] frameEntities) {
        population = new Individual[POPULATION_SIZE];
        Population.frameEntities = frameEntities;
        generatePopulation();
    }

    public Individual crossover(Individual parent1, Individual parent2, int pointer){
        Individual child = new Ant(frameEntities[pointer]);
        for (int i = 0; i < child.getBrain().getLayers().length; i++) {
            for (int row = 0; row < child.getBrain().getLayers()[i].getMatrix().getRowCount(); row++) {
                double parent = Math.random();
                if (parent <= 0.5) {
                    child.getBrain().getLayers()[i].getMatrix().setRow(parent1.getBrain().getLayers()[i].getMatrix().getRow(i),i);
                }
                else{
                    child.getBrain().getLayers()[i].getMatrix().setRow(parent2.getBrain().getLayers()[i].getMatrix().getRow(i),i);
                }
            }
        }
        child.mutate();
        return child;
    }

    protected void sortByFitness(){
        for (int i = 0; i < population.length; i++) {
            for (int j = 0; j < population.length; j++) {
                if (population[i].getScore() > population[j].getScore()) {
                    Individual temp = population[i];
                    population[i] = population[j];
                    population[j] = temp;

                    FrameEntity tempFrameEntity = frameEntities[i];
                    frameEntities[i] = frameEntities[j];
                    frameEntities[j] = tempFrameEntity;
                }
            }
        }
    }
}
