package NeuroEvolution.Environment;

import NeuroEvolution.Entities.FrameEntity;
import NeuroEvolution.Population.Population;

import javax.swing.*;

import static NeuroEvolution.Population.Population.POPULATION_SIZE;

public abstract class Environment extends JFrame implements EnvironmentInterface{
    //SETTINGS:
    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 1000;
    protected static final int NUMBER_OF_GENERATIONS = 1000;
    protected static final int NUMBER_OF_ITERATIONS_PER_GENERATION = 2500;
    protected static final int MILLISECONDS_PER_ITERATION = 1;
    protected static JButton showFittestEntitiesButton;

    public Environment() {
        this.setLayout(null);
        this.setSize(WINDOW_WIDTH+200,WINDOW_HEIGHT);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setTitle("0. generáció");

        showFittestEntitiesButton = new JButton("Show fittest entities");
        showFittestEntitiesButton.setBounds(WINDOW_WIDTH, 20,160,50);
        showFittestEntitiesButton.addActionListener(x -> {
            showFittestEntitiesButtonAction();
        });
        this.add(showFittestEntitiesButton);
        this.setVisible(true);
    }

    public void showFittestEntitiesButtonAction(){
        if (Population.getFrameEntities()[Population.getFrameEntities().length-1].isVisible()) {
            for (int i = Population.NUMBER_OF_ELITE_INDIVIDUALS+1; i < POPULATION_SIZE; i++) {
                Population.getFrameEntities()[i].setVisible(false);
            }
        }
        else{
            for (int i = Population.NUMBER_OF_ELITE_INDIVIDUALS+1; i < POPULATION_SIZE; i++) {
                Population.getFrameEntities()[i].setVisible(true);
            }
        }
    };




}
