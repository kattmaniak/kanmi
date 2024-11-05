package com.etf.lab3.kanmi;

import com.etf.lab3.kanmi.objects.Block;
import javafx.geometry.Point3D;
import javafx.scene.Node;

import java.util.ArrayList;

public interface Enemy {
    public void move(double playerX, double playerZ, ArrayList<Node> blocks);
}
