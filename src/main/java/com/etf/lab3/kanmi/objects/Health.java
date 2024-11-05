package com.etf.lab3.kanmi.objects;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.util.Duration;

public class Health extends Group {

    public Health (double x, double z) {
        Box horizontal = new Box(8, 2, 2);
        Box vertical = new Box(2, 8, 2);
        PhongMaterial material = new PhongMaterial(Color.RED);
        horizontal.setMaterial(material);
        vertical.setMaterial(material);
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(3), this);
        rotateTransition.setAxis(new Point3D(0, 1, 0));
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(Integer.MAX_VALUE);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.play();

        this.getChildren().addAll(horizontal, vertical);
        this.setTranslateX(x);
        this.setTranslateZ(z);
    }
}
