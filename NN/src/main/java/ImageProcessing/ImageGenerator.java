package ImageProcessing;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;

import Math.Matrix;
import NeuralNetwork.Model.Model;

public class ImageGenerator extends JPanel {

    private BufferedImage canvas;
    private JLabel label;
    private static final DecimalFormat decfor = new DecimalFormat("0.000");

    public ImageGenerator(int width, int height, Matrix matrix, Matrix prediction) {
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.setLayout(null);
        for (int i = 0; i < 10; i++) {

            label = new JLabel(i+": "+decfor.format(prediction.get(i,0)*100)+"%");
            label.setBounds(430,50,150,10+(i*50));
            this.add(label);
        }

        fillCanvas(matrix);
    }
    public ImageGenerator(int width, int height, Matrix matrix) {
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.setLayout(null);
        for (int i = 0; i < 10; i++) {
            //label.setBounds(430,50,150,10+(i*50));
            //this.add(label);
        }

        fillCanvas(matrix);
    }

    public Dimension getPreferredSize() {
        return new Dimension(canvas.getWidth(), canvas.getHeight());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(canvas, null, null);
    }

    public void fillCanvas(Matrix input) {
        int stride = 420 / input.getColCount();
        for (int x = 0; x < 420; x = x + stride) {
            for (int y = 0; y < 420; y = y + stride) {
                int rgb = (int) input.get(x/stride,y/stride);
                Color color = new Color(rgb,rgb,rgb);
                for (int i = x; i < x+stride; i++) {
                    for (int j = y; j < y+stride; j++) {
                        canvas.setRGB(j, i, color.getRGB());
                    }
                }
            }
        }
        repaint();
    }

}
