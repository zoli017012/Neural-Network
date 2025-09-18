package NeuroEvolution;

import NeuroEvolution.Environment.AntEnvironment;
import NeuroEvolution.Environment.Environment;

public class AntFarm {
    public static void main(String[] args) throws InterruptedException {
        Environment env = new AntEnvironment();
        env.start();
    }
}