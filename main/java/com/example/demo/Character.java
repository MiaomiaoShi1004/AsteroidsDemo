package com.example.demo;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public abstract class Character {
    // The Polygon object represents the object
    protected Polygon character;
    // the Point2D object represents the movement of the object. ( the point2d class has both x and y coordinates.)
    protected Point2D movement;

    private boolean alive = true;

    public Character(Polygon polygon, int x, int y) {
        this.character = polygon;
        this.character.setTranslateX(x);
        this.character.setTranslateY(y);
        this.movement = new Point2D(0, 0);
    }

    public Polygon getCharacter() {
        return character;
    }

    public void SetCharacterXY(int x, int y) {
        this.character.setTranslateX(x);
        this.character.setTranslateY(y);
    }

//  currently the move method of a Character is private, to access to it, add getMovement() and setMovement
    public Point2D getMovement() {
        return movement;
    }

    public void setMovement(Point2D movement) {
        this.movement = movement;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void turnLeft() {
        this.character.setRotate(this.character.getRotate() - 5);
    }

    public void turnRight() {
        this.character.setRotate(this.character.getRotate() + 5);
    }


    public void move() {
        this.character.setTranslateX(this.character.getTranslateX() + this.movement.getX());
        this.character.setTranslateY(this.character.getTranslateY() + this.movement.getY());

        //  make the characters stay on screen
        if (this.character.getTranslateX() < 0) {
            this.character.setTranslateX(this.character.getTranslateX() + Main.WIDTH);
        }
        if (this.character.getTranslateX() > Main.WIDTH) {
            this.character.setTranslateX(this.character.getTranslateX() % Main.WIDTH);
        }

        if (this.character.getTranslateY() < 0) {
            this.character.setTranslateY(this.character.getTranslateY() + Main.HEIGHT);
        }
        if (this.character.getTranslateY() > Main.HEIGHT) {
            this.character.setTranslateY(this.character.getTranslateY() % Main.HEIGHT);
        }
    }

    public void accelerate() {
        // converts degrees into radians
        double changeX = Math.cos(Math.toRadians(this.character.getRotate()));
        double changeY = Math.sin(Math.toRadians(this.character.getRotate()));

        changeX *= 0.04;
        changeY *= 0.04;

        this.movement = this.movement.add(changeX, changeY);
    }

    public boolean collide(Character other){
        Shape collisionArea = Shape.intersect(this.character, other.getCharacter());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }
    
    public void addStyleClass(String styleClass) {
        ((Polygon) this.getCharacter()).getStyleClass().add(styleClass);
    }
    
    public void removeStyleClass(String styleClass) {
    	((Polygon) this.getCharacter()).getStyleClass().remove(styleClass);
    }

}
