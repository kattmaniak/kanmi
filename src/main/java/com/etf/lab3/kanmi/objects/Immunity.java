package com.etf.lab3.kanmi.objects;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;

public class Immunity extends Group {
    public Immunity(double x, double z) {
        Sphere topPill = new Sphere(2.5);
        topPill.setTranslateY(-2.5);
        Sphere bottomPill = new Sphere(2.5);
        bottomPill.setTranslateY(2.5);
        Cylinder topHalf = new Cylinder(2.5, 2.5);
        topHalf.setTranslateY(-1.25);
        Cylinder bottomHalf = new Cylinder(2.5, 2.5);
        bottomHalf.setTranslateY(1.25);
        PhongMaterial topMaterial = new PhongMaterial(Color.PURPLE);
        PhongMaterial bottomMaterial = new PhongMaterial(Color.WHITE);
        topPill.setMaterial(topMaterial);
        bottomPill.setMaterial(bottomMaterial);
        topHalf.setMaterial(topMaterial);
        bottomHalf.setMaterial(bottomMaterial);
        Group pill = new Group();
        pill.getChildren().addAll(topPill, bottomPill, topHalf, bottomHalf);
        pill.setRotationAxis(new Point3D(0,0,1));
        pill.setRotate(45);
        this.getChildren().add(pill);
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
