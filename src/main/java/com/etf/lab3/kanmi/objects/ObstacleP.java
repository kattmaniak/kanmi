package com.etf.lab3.kanmi.objects;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class ObstacleP extends Group {
    public ObstacleP(double x, double z, boolean isRotated) {
        Box top = new Box(10, 2, 2);
        Box bottom1 = new Box(2, 10, 2);
        Box bottom2 = new Box(2, 10, 2);
        bottom1.setTranslateX(-4);
        bottom1.setTranslateY(4);
        bottom2.setTranslateX(4);
        bottom2.setTranslateY(4);
        PhongMaterial material = new PhongMaterial(Color.SADDLEBROWN);
        top.setMaterial(material);
        bottom1.setMaterial(material);
        bottom2.setMaterial(material);
        this.getChildren().addAll(top, bottom1, bottom2);

        setTranslateX(x);
        setTranslateZ(z);
        this.setRotationAxis(new Point3D(0, 1, 0));
        if (isRotated) {
            setRotate(90);
        }
    }
}
