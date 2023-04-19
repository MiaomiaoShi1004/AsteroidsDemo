package com.example.demo;

import javafx.scene.shape.Polygon;

// projectiles have a shape, a direction and movement
public class Projectile extends Character{

    private double traveledDistance = 0; //keep track of the distance that projectile travels
    private final double maxDistance = 30; // Set a maximum distance the projectile can travel
    private int lives;

    public Projectile(int x, int y) {
        super(new Polygon(2, -2, 2, 2, -2, 2, -2, -2), x, y);
    }

    @Override
    public void move() {
        this.getCharacter().setTranslateX(this.getCharacter().getTranslateX() + this.getMovement().getX());
        this.getCharacter().setTranslateY(this.getCharacter().getTranslateY() + this.getMovement().getY());

        this.traveledDistance += this.getMovement().magnitude();
        if (this.traveledDistance >= this.maxDistance) {
            this.setAlive(false);
        }
    }
    public void setLives(int live){
        this.lives = live;
    }
    public int getLives() {return this.lives;}
    public void loselife(){
        this.lives--;
    }
}
