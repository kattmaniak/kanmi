package com.etf.lab3.kanmi;

import com.etf.lab3.kanmi.objects.Block;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;

import java.util.ArrayList;

public class FollowerEnemy extends Group implements Enemy{

    public FollowerEnemy(double x, double z) {
        this.setRotationAxis(new Point3D(0,1,0));
        Sphere head = new Sphere(3);
        head.setMaterial(new PhongMaterial(Color.DARKRED));
        head.setTranslateY(2.5);
        Cylinder body = new Cylinder(3, 10);
        body.setMaterial(new PhongMaterial(Color.DARKRED));
        body.setTranslateY(7.5);
        Sphere eye1 = new Sphere(1);
        eye1.setMaterial(new PhongMaterial(Color.BLACK));
        eye1.setTranslateX(-0.75);
        eye1.setTranslateZ(-2);
        eye1.setTranslateY(4);
        Sphere eye2 = new Sphere(1);
        eye2.setMaterial(new PhongMaterial(Color.BLACK));
        eye2.setTranslateX(0.75);
        eye2.setTranslateZ(-2);
        eye2.setTranslateY(4);
        this.getChildren().addAll(head, body, eye1, eye2);
        setTranslateX(x);
        setTranslateZ(z);
    }
    @Override
    public void move(double playerX, double playerZ, ArrayList<Node> blocks) {
        this.setRotate(-Math.toDegrees(Math.atan2(playerZ - this.getTranslateZ(), playerX - this.getTranslateX())) -90);
        double oldX = this.getTranslateX();
        double oldZ = this.getTranslateZ();
        double x = playerX - this.getTranslateX();
        double z = playerZ - this.getTranslateZ();
        double distance = Math.sqrt(x * x + z * z);
        if (distance > 0) {
            x /= distance;
            z /= distance;
        }
        this.setTranslateX(this.getTranslateX() + x*0.3);
        this.setTranslateZ(this.getTranslateZ() + z*0.3);
        for (Node block : blocks) {
            if (this.getBoundsInParent().intersects(block.getBoundsInParent())) {
                this.setTranslateX(oldX);
                this.setTranslateZ(oldZ);
                break;
            }
        }
    }
}
