package com.example.demo;

import javafx.scene.shape.Polygon;
import java.util.Random;
import java.util.LinkedList;


public class Alien extends Character{

    // represent the current life of the alien ship
    private Integer lives;

    public Alien(int x, int y, int health) {
        super(new Polygon(0,0, 75,0, 60,-15, 45,-15, 45,-22.5, 30,-22.5, 30,-15, 15,-15), x, y);
        this.lives = health;

        // make the alien ship move
        super.getCharacter().setRotate(0);
        Random rnd = new Random();
        int accelerationAmount = 10;
        for (int i = 0; i < accelerationAmount; i++) {
            accelerate();
        }
    }

    @Override
    public void move() {
        this.character.setTranslateX(this.character.getTranslateX() + this.movement.getX());
        this.character.setTranslateY(this.character.getTranslateY() + this.movement.getY());
        // the ship will reset after it travels two times of the x distance
        if (this.character.getTranslateX() > AsteroidsApplication.WIDTH) {
            this.character.setTranslateX(0);
        }

    }

     public Integer getLives(){
        return lives;
     }

    public void setLives(int livesNum){
        lives = livesNum;
    }


}
