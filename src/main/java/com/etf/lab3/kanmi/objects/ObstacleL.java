package com.etf.lab3.kanmi.objects;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class ObstacleL extends Group {
    public ObstacleL(double x, double z, boolean isRotated) {
        Box top = new Box(10, 2, 2);
        Box bottom = new Box(2, 10, 2);
        bottom.setTranslateX(-4);
        bottom.setTranslateY(4);
        PhongMaterial material = new PhongMaterial(Color.SADDLEBROWN);
        top.setMaterial(material);
        bottom.setMaterial(material);
        Group obstacle = new Group();
        obstacle.setRotationAxis(new Point3D(0, 0, 1));
        obstacle.setRotate(225);
        obstacle.setTranslateY(-4);
        obstacle.getChildren().addAll(top, bottom);
        this.getChildren().addAll(obstacle);
        setTranslateX(x);
        setTranslateZ(z);
        this.setRotationAxis(new Point3D(0, 1, 0));
        if (isRotated) {
            setRotate(90);
        }
    }
}
