package NeuroEvolution.Environment;

import NeuroEvolution.Entities.AntFrameEntity;
import NeuroEvolution.Entities.FrameEntity;
import NeuroEvolution.Entities.FoodFrameEntity;
import NeuroEvolution.Individuals.Ant;
import NeuroEvolution.Population.AntPopulation;
import NeuroEvolution.Population.Population;

import java.awt.*;
import Math.Matrix;

import static NeuroEvolution.Population.Population.POPULATION_SIZE;

public class AntEnvironment extends Environment{
    private FrameEntity food = new FoodFrameEntity();
    private int foodX;
    private int foodY;
    private Rectangle foodRectangle;
    Population population;

    @Override
    public FrameEntity[] createEntities() {
        FrameEntity[] entities = new AntFrameEntity[POPULATION_SIZE];
        for (int i = 0; i < POPULATION_SIZE; i++) {
            FrameEntity newFrameEntity = new AntFrameEntity();
            entities[i] = newFrameEntity;
            this.add(newFrameEntity);
        }
        return entities;
    }

    @Override
    public void start() throws InterruptedException {
        FrameEntity[] entities = createEntities();
        population = new AntPopulation(entities);
        int iteration = 1;
        spawnFood();
        for (int generation = 0; generation < NUMBER_OF_GENERATIONS; generation++) {
            while (iteration <= NUMBER_OF_ITERATIONS_PER_GENERATION) {
                if (iteration % (NUMBER_OF_ITERATIONS_PER_GENERATION / 5) == 0) {
                    for (int i = 0; i < population.getPopulation().length; i++) {
                        Ant ant = (Ant) population.getPopulation()[i];
                        if (ant.getEntity().getBounds().intersects(foodRectangle)) {
                            ant.addScore(1);
                        }
                    }
                    respawnFood();
                }
                for (int i = 0; i < population.getPopulation().length; i++) {
                    Ant ant = (Ant) population.getPopulation()[i];
                    Matrix input = calculateInput(ant);
                    Matrix output = ant.makeDecision(input);
                    ant.reactToEnvironment(output);
                }
                Thread.sleep(MILLISECONDS_PER_ITERATION);
                iteration++;
            }
            int successfulAnts = 0;
            for (int i = 0; i < population.getPopulation().length; i++) {
                if (population.getPopulation()[i].getScore() >= 5) {
                    successfulAnts++;
                }
            }
            System.out.println("A "+(generation + 1)+" generációban tökéletesen teljesítő hangyák száma: "+successfulAnts);
            iteration = 1;
            population.selection();
            this.setTitle((generation + 1) + ". generáció");
        }
    }
    private Matrix calculateInput(Ant ant){
        double distanceFromFoodX = foodX - ant.getEntity().getX();
        double distanceFromFoodY = foodY - ant.getEntity().getY();
        return new Matrix(new double[][]{
                {distanceFromFoodX},
                {distanceFromFoodY},
        });
    }
    private void spawnFood(){
        food = new FoodFrameEntity();
        foodX = food.getX();
        foodY = food.getY();
        foodRectangle = food.getBounds();
        this.add(food);
        repaint();
    }
    private void respawnFood(){
        this.remove(food);
        spawnFood();
    }
}
