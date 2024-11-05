package com.etf.lab3.kanmi.objects;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class Block extends Group {
    public Block(double x, double z) {
        Box box = new Box(20, 20, 20);
        box.setMaterial(new PhongMaterial(Color.SADDLEBROWN));
        this.getChildren().add(box);
        this.setTranslateX(x);
        this.setTranslateZ(z);
    }
}
