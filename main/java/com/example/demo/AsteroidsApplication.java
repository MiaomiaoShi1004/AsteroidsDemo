package com.example.demo;

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

    // game window parameter
    public static int WIDTH = 300;
    public static int HEIGHT = 200;
    public static List<Asteroid> asteroids = new ArrayList<>();


    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = new Pane();
        pane.setPrefSize(WIDTH, HEIGHT);

        // present the ship in the screen
        Ship ship = new Ship(WIDTH / 2, HEIGHT / 2);  // init the ship to be in the center of the windows
        // multiple asteroids
        for (int i = 0; i < 5; i++) {
            Random rnd = new Random();
            Asteroid asteroid = new Asteroid(rnd.nextInt(WIDTH / 3), rnd.nextInt(HEIGHT));
            asteroids.add(asteroid);
        }
        // multiple projectiles (empty at first)
        List<Projectile> projectiles = new ArrayList<>();

        // Present the objects in the screen
        pane.getChildren().add(ship.getCharacter());
        asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getCharacter()));

        // set the display windows
        Scene scene = new Scene(pane);
        stage.setTitle("Asteroids!");
        stage.setScene(scene);
        stage.show();

        // event handler to rotate the ship from keyboard input (keep a record of pressed keys instead of multiple individuals key input)
        Map<KeyCode, Boolean> pressedKeys = new HashMap<>();
        scene.setOnKeyPressed(event -> {
            pressedKeys.put(event.getCode(), Boolean.TRUE);
        });
        scene.setOnKeyReleased(event -> {
            pressedKeys.put(event.getCode(), Boolean.FALSE);
        });
        // move the ship in case the left or right key is pressed.
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
                    ship.turnLeft();
                }
                if(pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
                    ship.turnRight();
                }
                if(pressedKeys.getOrDefault(KeyCode.UP, false)) {
                    ship.accelerate();
                }
                if(pressedKeys.getOrDefault(KeyCode.H, false)) {
                    ship.hyperSpaceJump();
                }

                // press space for shooting
                // && limit the number of projectiles
                if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && projectiles.size() < 3) {
                    // create a projectile and its direction is the same as the ship's direction.
                    Projectile projectile = new Projectile((int) ship.getCharacter().getTranslateX(), (int) ship.getCharacter().getTranslateY());
                    projectile.getCharacter().setRotate(ship.getCharacter().getRotate());
                    projectiles.add(projectile);

                    projectile.accelerate();
                    projectile.setMovement(projectile.getMovement().normalize().multiply(3));
                    // Present the projectile in the screen
                    pane.getChildren().add(projectile.getCharacter());
                }

                // ship to move
                ship.move();
                // asteroid to move
                asteroids.forEach(asteroid -> asteroid.move());
                // projectile to move
                projectiles.forEach(projectile -> projectile.move());

                // projectile hit asteroids
                projectiles.forEach(projectile -> {
                    List<Asteroid> collisions = asteroids.stream().filter(asteroid -> asteroid.collide(projectile)).collect(Collectors.toList());
                    // Removed asteroid from the asteroid list
                    collisions.stream().forEach(collided -> {
                        asteroids.remove(collided);
                        pane.getChildren().remove(collided.getCharacter());
                    });
                });

                // stops the application if a collision happens
                asteroids.forEach(asteroid -> {
                    if (ship.collide(asteroid)) {
                        stop();
                    }
                });
            }
        }.start();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
