package com.example.demo;

import javafx.scene.shape.Polygon;

import java.util.Random;

public class Asteroid extends Character{

    // Hold a private rotationalMovement so that each time an asteroid moves, it also rotates a little.
    // rotationalMovement represent the fixed value which is added to the "Rotate" everytime it moves.
    private double rotationalMovement;

    public Asteroid(int x, int y) {

        // invoke the super class(Character class)'s construction method
        super(new PolygonFactory().createPolygon(), x, y);
        // set the random rotate
        Random rnd = new Random();
        super.getCharacter().setRotate(rnd.nextInt(360));
        // set the random acceleration
        int accelerationAmount = 1 + rnd.nextInt(10);
        for (int i = 0; i < accelerationAmount; i++) {
            accelerate();
        }

        this.rotationalMovement = 0.5 - rnd.nextDouble();
    }

    @Override
    public void move() {
        super.move();
        super.getCharacter().setRotate(super.getCharacter().getRotate() + rotationalMovement);
    }
}
