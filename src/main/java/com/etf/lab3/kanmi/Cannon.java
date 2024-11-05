package com.etf.lab3.kanmi;

import com.etf.lab3.kanmi.objects.Bullet;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;

public class Cannon extends Group {
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;
    public Cannon(double x, double z){
        Cylinder cannon = new Cylinder(2, 10);
        PhongMaterial material = new PhongMaterial(Color.ORANGERED);
        cannon.setMaterial(material);
        cannon.setRotate(90);
        this.getChildren().add(cannon);
        this.setRotationAxis(new Point3D(0, 1, 0));
        left = x<0 && Math.abs(z)<Math.abs(x);
        right = x>0 && Math.abs(z)<Math.abs(x);
        down = z<0 && Math.abs(z)>Math.abs(x);
        up = z>0 && Math.abs(z)>Math.abs(x);
        this.setRotate(left?0:right?180:up?90:270);
        setTranslateX(x);
        setTranslateZ(z);
    }
    public Bullet shoot(){
        Point3D speed = new Point3D(left?1:right?-1:0, 0, down?1:up?-1:0);
        return new Bullet(this.getTranslateX()+(left?1:right?-1:0)*5, this.getTranslateZ()+(down?1:up?-1:0)*5, speed);
    }
}
