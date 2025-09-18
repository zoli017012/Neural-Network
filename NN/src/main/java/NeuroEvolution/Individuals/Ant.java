package NeuroEvolution.Individuals;
import Math.Matrix;
import NeuralNetwork.ActivationFunction.ActivationFunction;
import NeuralNetwork.Layer.Dense;
import NeuralNetwork.Layer.Layer;
import NeuralNetwork.Model.ClassificationModel;
import NeuralNetwork.Model.Model;
import NeuroEvolution.Entities.FrameEntity;
import NeuroEvolution.Environment.Environment;

import javax.swing.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Ant extends Individual{
    private static final int STEP_SIZE = 4;
    private static final int WINDOW_HEIGHT = Environment.WINDOW_HEIGHT - 50;
    private static final int WINDOW_WIDTH = Environment.WINDOW_WIDTH - 50;
    private static Model brain;
    ImageIcon up = new ImageIcon(Paths.get("").toAbsolutePath()+"\\src\\main\\resources\\images\\antUp.png");
    ImageIcon left = new ImageIcon(Paths.get("").toAbsolutePath()+"\\src\\main\\resources\\images\\antLeft.png");
    ImageIcon right = new ImageIcon(Paths.get("").toAbsolutePath()+"\\src\\main\\resources\\images\\antRight.png");
    ImageIcon down = new ImageIcon(Paths.get("").toAbsolutePath()+"\\src\\main\\resources\\images\\antDown.png");

    public Ant(FrameEntity frameEntity) {
        super(frameEntity, setupBrain());
        frameEntity.setIcon(up);
    }

    private static void createBrain(){
        brain = new ClassificationModel(new Layer[]{
                new Dense(5,2, ActivationFunction.softmax)
        });
    }

    public static Model setupBrain() {
        createBrain();
        return brain;
    }

    @Override
    public void reactToEnvironment(Matrix outputs) {
        switch (outputs.argmax()){
            case 0: //if (frameEntity.getX() - STEP_SIZE >= 0) {
                frameEntity.setBounds(frameEntity.getX() - STEP_SIZE, frameEntity.getY(), 10, 10);
                frameEntity.setIcon(left);
            //}
            break; // left
            case 1: //if (frameEntity.getX() + STEP_SIZE <= WINDOW_WIDTH) {
                frameEntity.setBounds(frameEntity.getX() + STEP_SIZE, frameEntity.getY(), 10, 10);
                frameEntity.setIcon(right);
            //}
            break; // right
            case 2: //if (frameEntity.getY() - STEP_SIZE >= 0) {
                frameEntity.setBounds(frameEntity.getX(), frameEntity.getY() - STEP_SIZE, 10, 10);
                frameEntity.setIcon(up);
            //}
            break; // up
            case 3: //if (frameEntity.getY() + STEP_SIZE <= WINDOW_HEIGHT) {
                frameEntity.setBounds(frameEntity.getX(), frameEntity.getY() + STEP_SIZE, 10, 10);
                frameEntity.setIcon(down);
            //}
            break; // down
            case 4:
                break;
        }
    }
}
