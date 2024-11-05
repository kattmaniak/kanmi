package com.etf.lab3.kanmi.objects;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.util.Duration;

public class GreenCoin extends Group
{
    public GreenCoin(double x, double z)
    {
        Cylinder outerCylinder = new Cylinder(4, 1);
        outerCylinder.setMaterial(new PhongMaterial(Color.LAWNGREEN));
        outerCylinder.setRotate(90);

        Cylinder innerCylinder = new Cylinder(3, 1);
        innerCylinder.setMaterial(new PhongMaterial(Color.GREEN));
        innerCylinder.setRotate(90);

        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(3), this);
        rotateTransition.setAxis(new Point3D(0, 1, 0));
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(Integer.MAX_VALUE);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.play();

        setTranslateX(x);
        setTranslateZ(z);
        getChildren().addAll(outerCylinder, innerCylinder);
    }
}
