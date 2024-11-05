package com.etf.lab3.kanmi.objects;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;

public class JokerCoin extends Group
{
    public JokerCoin(double x, double z)
    {
        Cylinder outerCylinder = new Cylinder(4, 1);
        outerCylinder.setMaterial(new PhongMaterial(Color.WHITE));
        outerCylinder.setRotate(90);
        Group head = new Group();
        head.getChildren().add(outerCylinder);
        head.setRotationAxis(new Point3D(0, 1, 0));
        head.setRotate(90);

        Sphere leftEye = new Sphere(1);
        leftEye.setTranslateX(-1.75);
        leftEye.setTranslateY(-1.75);

        Sphere rightEye = new Sphere(1);
        rightEye.setTranslateX(1.75);
        rightEye.setTranslateY(-1.75);

        Sphere nose = new Sphere(1.25);

        Box mouthLeft = new Box(3, 1.1, 1.1);
        mouthLeft.setRotationAxis(new Point3D(0,0,1));
        mouthLeft.setRotate(45);
        mouthLeft.setTranslateY(1.5);
        mouthLeft.setTranslateX(-2);

        Box mouthRight = new Box(3, 1.1, 1.1);
        mouthRight.setRotationAxis(new Point3D(0,0,1));
        mouthRight.setRotate(-45);
        mouthRight.setTranslateY(1.5);
        mouthRight.setTranslateX(2);

        Box mouthMiddle = new Box(2.5, 1.1, 1.1);
        mouthMiddle.setTranslateY(2.5);

        PhongMaterial material = new PhongMaterial(Color.BLACK);
        PhongMaterial noseMaterial = new PhongMaterial(Color.RED);
        leftEye.setMaterial(material);
        rightEye.setMaterial(material);
        nose.setMaterial(noseMaterial);
        mouthLeft.setMaterial(material);
        mouthRight.setMaterial(material);
        mouthMiddle.setMaterial(material);

        getChildren().addAll(head, leftEye, rightEye, nose, mouthLeft, mouthRight, mouthMiddle);

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
