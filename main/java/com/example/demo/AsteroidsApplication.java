package com.example.demo;
//https://java-programming.mooc.fi/part-14/3-larger-application-asteroids

import java.util.*;

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

    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = new Pane();
        pane.setPrefSize(600, 400);
//
////        present the ship in the screen
//        Polygon ship = new Polygon(-5, -5, 10, 0, -5, 5);
//        ship.setTranslateX(300);
//        ship.setTranslateY(200);
//
//        pane.getChildren().add(ship);

        Ship ship = new Ship(150, 100);
        pane.getChildren().add(ship.getCharacter());

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
//        Point2D movement = new Point2D(1, 0);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
//                    ship.setRotate(ship.getRotate() - 5);
                    ship.turnLeft();
                }

                if(pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
//                    ship.setRotate(ship.getRotate() + 5);
                    ship.turnRight();
                }

//                ship.setTranslateX(ship.getTranslateX() + movement.getX());
                ship.move();
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
