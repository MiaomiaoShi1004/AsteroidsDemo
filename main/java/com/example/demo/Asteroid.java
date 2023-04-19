package com.example.demo;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

import java.util.*;

import static com.example.demo.Game.asteroids;

public class Asteroid extends Character{
    private Polygon asteroid;
    private Point2D movement;
    private double rotationalMovement;
    private int x;
    private int y;
    private int size; // 1 small, 2 medium, 3 large
    private int width;
    private int height;
    private int lives;


    public Asteroid(int size, int x, int y) {
        super(new PolygonFactory().createPolygon(size), x, y);

        Random rnd = new Random();

        /*if (size ==3 || size == 2){
            Asteroid asteroid1 = new Asteroid(size-1, x, y);
            Asteroid asteroid2 = new Asteroid(size-1, x, y);
            asteroid1.addStyleClass("asteroid");
            asteroid2.addStyleClass("asteroid");
            asteroids.add(asteroid1);
            asteroids.add(asteroid2);
        }*/
        super.getCharacter().setRotate(rnd.nextInt(360));

        int accelerationAmount = 1 + rnd.nextInt(10);
        for (int i = 0; i < accelerationAmount; i++) {
            accelerate();
        }
        this.size = size;
        this.rotationalMovement = 0.5 - rnd.nextDouble();
    }

    public int getSize(){
        return this.size;
    }

    public ArrayList<Double> getXY(){
        ArrayList<Double> position = new ArrayList<>();
        position.add(this.getCharacter().getTranslateX());
        position.add(this.getCharacter().getTranslateY());
        return position;
    }
    public int getLives() {return this.lives;}
    public void losslive(){
        this.lives--;
    }
    public void setLives(int live){
        this.lives = live;
    }

    @Override
    public void move() {
        super.move();
        super.getCharacter().setRotate(super.getCharacter().getRotate() + rotationalMovement);
    }
}

