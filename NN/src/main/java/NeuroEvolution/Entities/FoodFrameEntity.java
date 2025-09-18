package NeuroEvolution.Entities;

import javax.swing.*;
import java.nio.file.Paths;

public class FoodFrameEntity extends FrameEntity {
    private static final ImageIcon icon = new ImageIcon(Paths.get("").toAbsolutePath()+"\\src\\main\\resources\\images\\apple.png");
    public FoodFrameEntity(){
        this.setIcon(icon);
    }
}
