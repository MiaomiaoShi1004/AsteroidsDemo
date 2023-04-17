package com.example.demo;

import javafx.scene.shape.Polygon;

// projectiles have a shape, a direction and movement
public class Projectile extends Character{

    public Projectile(int x, int y) {
        super(new Polygon(2, -2, 2, 2, -2, 2, -2, -2), x, y);
    }

    @Override
    public void move() {
//        projectiles will be removed if going out of the window pane
        this.getCharacter().setTranslateX(this.getCharacter().getTranslateX() + this.getMovement().getX());
        this.getCharacter().setTranslateY(this.getCharacter().getTranslateY() + this.getMovement().getY());

    }
}
