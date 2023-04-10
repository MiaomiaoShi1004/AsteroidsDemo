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
import javafx.application.Platform;
import java.util.Random;




public class AsteroidsApplication extends Application {

    // game window parameter
    public static int WIDTH = 600;
    public static int HEIGHT = 400;
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
        Random rnd = new Random();

        // multiple projectiles (empty at first)
        List<Projectile> projectiles = new ArrayList<>();
        // multiple projectiles fired by alien (empty at first)
        List<Projectile> projectilesAlien = new ArrayList<>();
        // present the alien
        Alien alien = new Alien(0, 30, 2);

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
            double startTime = System.currentTimeMillis(); //set generate alien game start time
            int alienFlag = 0; //a flag for alien ship appearing
            double lastAlienBullet = 0; //a timer for fire shooting speed of alien
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
                projectilesAlien.forEach(projectile -> projectile.move());

                //select a random time to generate alien ship, and make it move
                if (System.currentTimeMillis()-startTime > rnd.nextInt(10000, 50000) && alienFlag == 0) {
                    alien.setLives(2); //set live for alien ship
                    pane.getChildren().add(alien.getCharacter()); //add alien ship to pane
                    alienFlag++; // alienFlag increment
                    startTime = System.currentTimeMillis(); // refresh start time
                }
                if (alien.getLives() > 0) { //if the time is in random time bound, live of alien will be set to 1, and make it move
                    // alien to move
                    alien.move();
                }
                // let the alien ship shooting toward the user ship every 3 seconds
                if (alien.getLives() != 0 && System.currentTimeMillis()-lastAlienBullet > 3000 && alienFlag == 1) {
                    System.out.println("Alien fire!!!");
                    Projectile projectile = new Projectile((int) alien.getCharacter().getTranslateX(), (int) alien.getCharacter().getTranslateY());
                    // calculate the direction
                    double userx = ship.getCharacter().getTranslateX();
                    double usery = ship.getCharacter().getTranslateY();
                    double alienx = alien.getCharacter().getTranslateX()+10;
                    double alieny = alien.getCharacter().getTranslateY()+10;
                    double distance = Math.sqrt((userx-alienx)*(userx-alienx) + (usery-alieny)*(usery-alieny));
                    double bulletRotate1 = Math.toDegrees(Math.asin(Math.abs((usery-alieny))/distance));
                    double bulletRotate2 = 180-Math.toDegrees(Math.asin(Math.abs((usery-alieny))/distance));
                    //set the direction of bullets from alien
                    if (userx > alienx) {
                        if (usery < alieny) {
                            projectile.getCharacter().setRotate(alien.getCharacter().getRotate()-bulletRotate1);
                        }
                        else {
                            projectile.getCharacter().setRotate(alien.getCharacter().getRotate()+bulletRotate1);
                        }
                    }
                    else {
                        if (usery < alieny) {
                            projectile.getCharacter().setRotate(alien.getCharacter().getRotate()-bulletRotate2);
                        }
                        else {
                            projectile.getCharacter().setRotate(alien.getCharacter().getRotate()+bulletRotate2);
                        }
                    }
                    projectilesAlien.add(projectile); //add alien bullet to general bullet list
                    projectile.accelerate();
                    projectile.setMovement(projectile.getMovement().normalize().multiply(2));
                    // Present the projectile in the screen
                    pane.getChildren().add(projectile.getCharacter());

                    lastAlienBullet = System.currentTimeMillis();
                }

                // stops the application if alien projectile hit ship
                projectilesAlien.forEach(projectile -> {
                    if (ship.collide(projectile)) {
                        System.out.println("Game over! You have benn hit by alien.");
                        stop();
                    }
                });

                // projectile hit asteroids
                projectiles.forEach(projectile -> {
                    List<Asteroid> collisions = asteroids.stream().filter(asteroid -> asteroid.collide(projectile)).collect(Collectors.toList());
                    // Removed asteroid from the asteroid list
                    collisions.stream().forEach(collided -> {
                        asteroids.remove(collided);
                        pane.getChildren().remove(collided.getCharacter());
                    });
                });

                // projectile hit Alien
                projectiles.forEach(projectile -> {
                    if (alien.collide(projectile)) {
                        int alienLife = alien.getLives();
                        if (alienLife > 0){
                            alien.setLives(alienLife-1);
                        }
                        alienLife = alien.getLives();
                        if (alienLife == 0){
                            System.out.println("The alien ship have 0 life");
                            pane.getChildren().remove(alien.getCharacter());
                        }
                    }
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
