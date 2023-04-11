package com.example.demo;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;


public class Ship extends Character{

    public Ship(int x, int y) {
        super(new Polygon(-5, -5, 10, 0, -5, 5),x, y);
    }

    //only ship will decelerate
    public void decelerate() {
        //decelerate the ship towards the direction (the angle)
        double changeX = Math.cos(Math.toRadians(this.getCharacter().getRotate()));
        double changeY = Math.sin(Math.toRadians(this.getCharacter().getRotate()));

        //to slow down the acceleration
        changeX *= -0.05;
        changeY *= -0.05;

        this.setMovement(this.getMovement().add(changeX, changeY));
    }
}
