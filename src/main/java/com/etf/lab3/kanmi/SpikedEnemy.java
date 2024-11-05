package com.etf.lab3.kanmi;

import com.etf.lab3.kanmi.objects.Block;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;

import java.util.ArrayList;

public class SpikedEnemy extends Group implements Enemy{
    private Point3D speed;

    public SpikedEnemy(double x, double z) {
        changeDirection();

        Sphere body = new Sphere(8);
        body.setMaterial(new PhongMaterial(Color.DARKSLATEGRAY));
        body.setTranslateY(10);
        TriangleMesh spike = new TriangleMesh();
        spike.getPoints().addAll(
                5,0,0,
                0,0,5,
                -5,0,0,
                0,0,-5,
                0,-10,0
        );
        spike.getTexCoords().addAll(0, 0);
        spike.getFaces().addAll(
                0,0,1,0,4,0,
                1,0,2,0,4,0,
                2,0,3,0,4,0,
                3,0,0,0,4,0
        );
        PhongMaterial spikeMaterial = new PhongMaterial(Color.DARKSLATEGRAY);
        MeshView spikeView = new MeshView(spike);
        spikeView.setMaterial(spikeMaterial);
        spikeView.setTranslateY(10);
        MeshView spikeView2 = new MeshView(spike);
        spikeView2.setMaterial(spikeMaterial);
        spikeView2.setRotationAxis(new Point3D(0,0,1));
        spikeView2.setRotate(30);
        spikeView2.setTranslateY(10);
        MeshView spikeView3 = new MeshView(spike);
        spikeView3.setMaterial(spikeMaterial);
        spikeView3.setRotationAxis(new Point3D(0,0,1));
        spikeView3.setRotate(-30);
        spikeView3.setTranslateY(10);
        MeshView spikeView4 = new MeshView(spike);
        spikeView4.setMaterial(spikeMaterial);
        spikeView4.setRotationAxis(new Point3D(1,0,0));
        spikeView4.setRotate(30);
        spikeView4.setTranslateY(10);
        MeshView spikeView5 = new MeshView(spike);
        spikeView5.setMaterial(spikeMaterial);
        spikeView5.setRotationAxis(new Point3D(1,0,0));
        spikeView5.setRotate(-30);
        spikeView5.setTranslateY(10);
        this.getChildren().addAll(body, spikeView, spikeView2, spikeView3, spikeView4, spikeView5);
        this.setTranslateX(x);
        this.setTranslateZ(z);
    }

    public void changeDirection() {
        this.speed = new Point3D(Math.random()>0.5?0.2:-0.2, 0, Math.random()>0.5?0.2:-0.2);
    }

    @Override
    public void move(double playerX, double playerZ, ArrayList<Node> blocks) {
        double oldX = this.getTranslateX();
        double oldZ = this.getTranslateZ();
        this.setTranslateX(this.getTranslateX() + this.speed.getX());
        this.setTranslateZ(this.getTranslateZ() + this.speed.getZ());
        for (Node block : blocks) {
            if (this.getBoundsInParent().intersects(block.getBoundsInParent())) {
                this.setTranslateX(oldX);
                this.setTranslateZ(oldZ);
                changeDirection();
                break;
            }
        }
        if(this.getTranslateX() < -250 || this.getTranslateX() > 250 || this.getTranslateZ() < -250 || this.getTranslateZ() > 250) {
            this.setTranslateX(oldX);
            this.setTranslateZ(oldZ);
            changeDirection();
        }
    }
}
