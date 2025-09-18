package NeuroEvolution.Environment;

import NeuroEvolution.Entities.FrameEntity;

public interface EnvironmentInterface {
    void start() throws InterruptedException;
    FrameEntity[] createEntities();
}
