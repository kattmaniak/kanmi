package com.etf.lab3.kanmi.objects;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.Box;
import javafx.util.Duration;

public class Energy extends Group {

    public Energy(double x, double z) {
        Box top = new Box(9, 2, 2);
        Box middle = new Box(8, 2, 2);
        Box bottom = new Box(9, 2, 2);
        top.setTranslateY(-3.5);
        bottom.setTranslateY(3.5);
        top.setRotationAxis(new Point3D(0, 0, 1));
        top.setRotate(45);
        bottom.setRotationAxis(new Point3D(0, 0, 1));
        bottom.setRotate(45);
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
