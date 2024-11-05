package com.etf.lab3.kanmi.objects;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.util.Duration;

public class Freeze extends Group {

    public Freeze(double x, double z) {
        Box top = new Box(20, 2, 2);
        Box middle = new Box(20, 2, 2);
        Box bottom = new Box(20, 2, 2);
        top.setRotationAxis(new Point3D(0, 0, 1));
        top.setRotate(-60);
        bottom.setRotationAxis(new Point3D(0, 0, 1));
        bottom.setRotate(60);
        PhongMaterial material = new PhongMaterial(Color.AQUA);
        top.setMaterial(material);
        middle.setMaterial(material);
        bottom.setMaterial(material);
        this.getChildren().addAll(top, middle, bottom);
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(3), this);
        rotateTransition.setAxis(new Point3D(0, 1, 0));
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(Integer.MAX_VALUE);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.play();

        setTranslateX(x);
        setTranslateZ(z);
    }
}
