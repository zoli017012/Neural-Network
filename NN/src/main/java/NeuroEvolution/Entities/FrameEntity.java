package NeuroEvolution.Entities;

import NeuroEvolution.Environment.Environment;

import javax.swing.*;

public abstract class FrameEntity extends JLabel {
    private static final int ENTITY_WIDTH = 10;
    private static final int ENTITY_HEIGHT = 10;
    public FrameEntity(){
        randomPosition();
    }
    public void randomPosition(){
        int randomXPosition = (int)(Math.random()*((Environment.WINDOW_WIDTH-50)/2))*2;
        int randomYPosition = (int)(Math.random()*((Environment.WINDOW_HEIGHT-50)/2))*2;
        this.setBounds(
                randomXPosition,
                randomYPosition,
                ENTITY_WIDTH,
                ENTITY_HEIGHT
        );
    }
}
