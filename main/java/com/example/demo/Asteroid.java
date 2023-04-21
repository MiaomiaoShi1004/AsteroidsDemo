package com.example.demo;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

import java.util.Random;

public class Asteroid extends Character{

    // Hold a private rotationalMovement so that each time an asteroid moves, it also rotates a little.
    // rotationalMovement represent the fixed value which is added to the "Rotate" everytime it moves.
    private double rotationalMovement;

    public double getPentagonSize() {
        return pentagonSize;
    }

    private double pentagonSize;

    public Asteroid(double pentagonSize, int x, int y) {

        // invoke the super class(Character class)'s construction method
        super(new CreatePolygon().createPolygon(pentagonSize), x, y);
        // set the random rotate
        Random rnd = new Random();
        super.getCharacter().setRotate(rnd.nextInt(360));
        this.pentagonSize = pentagonSize;

        // set the random acceleration
        int accelerationAmount = 1 + rnd.nextInt(10);
        for (int i = 0; i < accelerationAmount; i++) {
            accelerate();
        }
        this.rotationalMovement = 0.5 - rnd.nextDouble();
    }

    @Override
    public void move() {
        // Calculate speed factor based on pentagonSize
        double speedFactor = 0;
        if (this.pentagonSize == 10) {
            speedFactor = 1.8; // Fast speed
        } else if (this.pentagonSize == 20) {
            speedFactor = 1.5; // Medium speed
        } else if (this.pentagonSize == 30) {
            speedFactor = 1.0; // Slow speed (default)
        }

        // Apply speed factor to the movement
        this.character.setTranslateX(this.character.getTranslateX() + this.movement.getX() * speedFactor);
        this.character.setTranslateY(this.character.getTranslateY() + this.movement.getY() * speedFactor);

        // Call the rest of the move() code from the Character class
        super.move();

        // Update rotation as before
        super.getCharacter().setRotate(super.getCharacter().getRotate() + rotationalMovement);
    }
}
