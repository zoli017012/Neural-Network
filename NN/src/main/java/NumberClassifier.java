import DataProcessing.DataScaling.Normalizer;
import DataProcessing.Exceptions.DataSplitException;
import DataProcessing.TrainTestValidationSplit;
import ImageProcessing.ImageDisplayer;
import NeuralNetwork.ActivationFunction.ActivationFunction;
import NeuralNetwork.Layer.*;
import NeuralNetwork.Model.ClassificationModel;
import NeuralNetwork.Model.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Scanner;
import Math.Matrix;

public class NumberClassifier {
    public static void main(String[] args) throws FileNotFoundException, DataSplitException {
        File file = new File(Paths.get("").toAbsolutePath()+"\\train.csv");
        Scanner scanner = new Scanner(file);
        int trainingSamples = 10000;
        double[][] inArray = new double[trainingSamples][784];
        double[][] yArray = new double[1][trainingSamples];
        int pointer = 0;
        scanner.nextLine();
        while (pointer < trainingSamples) {
            String [] line = scanner.nextLine().split(",");
            yArray[0][pointer] = Double.parseDouble(line[0]);
            for (int i = 1; i < line.length; i++) {
                inArray[pointer][i-1] = Double.parseDouble(line[i]);
            }
            pointer++;
        }
        Matrix input = new Matrix(inArray);
        input = input.transpose();
        Normalizer.standardNormalization(input);
        Matrix output = new Matrix(yArray);

        TrainTestValidationSplit data = new TrainTestValidationSplit(input,output,0.6,0.3);
        Model model = new ClassificationModel(new Layer[]{
                new Dense(128,784 ,ActivationFunction.ReLu),
                new Dense(128,128 ,ActivationFunction.ReLu),
                new Dense(10,128, ActivationFunction.softmax)
        }
        ,data.trainX
        ,data.trainY
        ,data.validationX
        ,data.validationY
        ,256
        );

        model.train( 3);
        model.compile(data.testX, data.testY);


        file = new File(Paths.get("").toAbsolutePath()+"\\test.csv");
        scanner = new Scanner(file);
        for (int i = 0; i < 2000; i++) {
            scanner.nextLine();
        }
        inArray = new double[1][784];
        for (int i = 0; i < 50; i++) {
            String [] line = scanner.nextLine().split(",");
            for (int j = 0; j < line.length-1; j++) {
                inArray[0][j] = Double.parseDouble(line[j]);
            }
            input = new Matrix(inArray);
            input = input.transpose();
            Matrix pred = model.predict(input);
            input = input.reshape(28,28);
            ImageDisplayer imageDisplayer = new ImageDisplayer();
            imageDisplayer.showImage(input,pred);
        }
    }
}
