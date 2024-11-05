package com.etf.lab3.kanmi.objects;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class Bullet extends Group {
    private Point3D speed;
    public Bullet(double x, double z, Point3D speed){
        Sphere bullet = new Sphere(2);
        Sphere hitbox = new Sphere(3);
        hitbox.setVisible(false);
        PhongMaterial material = new PhongMaterial(Color.YELLOW);
        bullet.setMaterial(material);
        this.getChildren().addAll(bullet, hitbox);
        this.speed = speed;
        this.setTranslateX(x);
        this.setTranslateZ(z);
    }
    public void move(){
        this.setTranslateX(this.getTranslateX() + speed.getX());
        this.setTranslateZ(this.getTranslateZ() + speed.getZ());
    }
}
