package ImageProcessing;

import javax.swing.*;
import Math.Matrix;
import NeuralNetwork.Model.Model;

public class ImageDisplayer extends JFrame{
    public void showImage(Matrix image, Matrix prediction){
        ImageGenerator panel = new ImageGenerator(600, 420, image, prediction);
        this.add(panel);
        this.pack();
        this.setTitle("Osztályok nevét is tessék nézni");
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
