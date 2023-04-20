package com.example.demo;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

import java.util.Random;

import static com.example.demo.Main.*;
import static com.example.demo.Game.*;


public class Ship extends Character{

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    private int life = 3;


    // private variables for control the space jump time delay
    private boolean hyperjumpReady;
    private boolean hyperjumping;

    public Ship(int x, int y) {
        super(new Polygon(-5, -5, 10, 0, -5, 5),x, y);
        this.hyperjumpReady = true;
        this.hyperjumping = false;
    }

    public void hyperSpaceJump(){
        if (hyperjumpReady && !hyperjumping) {
            // Set hyperjump flag and reset ready flag
            hyperjumping = true;
            hyperjumpReady = false;

            // Set ship to random but secure position
            while (true){
                Boolean secureLocation = true;
                Random rnd = new Random();
                int width = rnd.nextInt(WIDTH);
                int height = rnd.nextInt(HEIGHT);
                Character characterTest = new Asteroid(width, height);
                // check the location whether it's safe
                for (Asteroid asteroid : asteroids){
                    if (characterTest.collide(asteroid)) {
                        secureLocation = false;
                        System.out.println("hyperspace jump hits a collision!");
                    }
                }

                if (secureLocation == true){
                    // have secure location
                    super.SetCharacterXY(width, height);
                    break;
                }
            }

            // Start hyperjump cooldown timer
            new java.util.Timer().schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    hyperjumpReady = true;
                    hyperjumping = false;
                }
            }, 4000);
        }
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