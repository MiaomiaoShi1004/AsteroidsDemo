package com.example.demo;
//https://java-programming.mooc.fi/part-14/3-larger-application-asteroids

import java.util.*;
import java.util.stream.Collectors;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class AsteroidsApplication extends Application {
    public static int WIDTH = 300;
    public static int HEIGHT = 200;

    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = new Pane();
        pane.setPrefSize(WIDTH, HEIGHT);

//        present the ship in the screen
        Ship ship = new Ship(WIDTH / 2, HEIGHT / 2);
//        multiple asteroids
        List<Asteroid> asteroids = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Random rnd = new Random();
            int xPos, yPos;

            do {
                xPos = rnd.nextInt(WIDTH);
                yPos = rnd.nextInt(HEIGHT);
            } while (Math.sqrt(Math.pow(ship.getCharacter().getTranslateX() - xPos, 2)
                    + Math.pow(ship.getCharacter().getTranslateY() - yPos, 2)) < 50);

            Asteroid asteroid = new Asteroid(xPos, yPos);
            asteroids.add(asteroid);
        }

//        present the asteroid in the screen
        pane.getChildren().add(ship.getCharacter());
        asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getCharacter()));

//        multiple projectiles (empty at first)
        List<Projectile> projectiles = new ArrayList<>();

        Scene scene = new Scene(pane);

//        event handler to rotate the ship
        Map<KeyCode, Boolean> pressedKeys = new HashMap<>();
        scene.setOnKeyPressed(event -> {
            pressedKeys.put(event.getCode(), Boolean.TRUE);
        });

        scene.setOnKeyReleased(event -> {
            pressedKeys.put(event.getCode(), Boolean.FALSE);
        });

//        move the ship
        new AnimationTimer() {
            private long lastBullet = 0; //for bullet to not be continuous
            @Override
            public void handle(long now) {

                if (pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
                    ship.turnLeft();
                }
                if (pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
                    ship.turnRight();
                }

                if (pressedKeys.getOrDefault(KeyCode.UP, false)) {
                    ship.accelerate();
                }

                if (pressedKeys.getOrDefault(KeyCode.DOWN, false)) {
                    ship.decelerate();
                }

                if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && (now - lastBullet > 300_000_000)) {
                    Projectile projectile = new Projectile((int) ship.getCharacter().getTranslateX(), (int) ship.getCharacter().getTranslateY());
                    projectile.getCharacter().setRotate(ship.getCharacter().getRotate());
                    projectiles.add(projectile);

                    projectile.accelerate();
                    projectile.setMovement(projectile.getMovement().normalize().multiply(3));

                    pane.getChildren().add(projectile.getCharacter());
                    lastBullet = now;
                }

                ship.move();
                asteroids.forEach(asteroid -> asteroid.move());
                projectiles.forEach(projectile -> projectile.move());

                //  Using iterator to safely remove collided projectiles and asteroids
                Iterator<Projectile> projectileIterator = projectiles.iterator();
                while (projectileIterator.hasNext()) {
                    Projectile projectile = projectileIterator.next();

                    Iterator<Asteroid> asteroidIterator = asteroids.iterator();
                    boolean projectileCollided = false;
                    while (asteroidIterator.hasNext()) {
                        Asteroid asteroid = asteroidIterator.next();
                        if (asteroid.collide(projectile)) {
                            projectileCollided = true;
                            asteroidIterator.remove();
                            pane.getChildren().remove(asteroid.getCharacter());
                        }
                    }

                    //  Removing collided projectiles
                    if (projectileCollided) {
                        projectileIterator.remove();
                        pane.getChildren().remove(projectile.getCharacter());
                    }

                }

                asteroids.forEach(asteroid -> {
                    if (ship.collide(asteroid)) {
                        stop();
                    }
                });
            }

        }.start();

        stage.setTitle("Asteroids!");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
