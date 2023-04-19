package com.example.demo;

import javafx.scene.shape.Polygon;

import java.util.Random;

// class is used to adding variation to the size of the asteroids and the locations of asteroids corners.
public class PolygonFactory {
    public Polygon createPolygon(int sizes) {
        Random rnd = new Random();

        if (sizes == 1){sizes = 10 + rnd.nextInt(100);}
        else if (sizes == 2){
        sizes = 25 + rnd.nextInt(100);}
        else if (sizes == 3){
        sizes = 50 + rnd.nextInt(100);}

        Polygon polygon = new Polygon();
        double c1 = Math.cos(Math.PI * 2 / 5);
        double c2 = Math.cos(Math.PI / 5);
        double s1 = Math.sin(Math.PI * 2 / 5);
        double s2 = Math.sin(Math.PI * 4 / 5);

        polygon.getPoints().addAll(
                (double) sizes, 0.0,
                sizes * c1, -1 * sizes * s1,
                -1 * sizes * c2, -1 * sizes * s2,
                -1 * sizes * c2, sizes * s2,
                sizes * c1, sizes * s1);

        for (int i = 0; i < polygon.getPoints().size(); i++) {
            int change = rnd.nextInt(5) - 2;
            polygon.getPoints().set(i, polygon.getPoints().get(i) + change);
        }

        return polygon;
    }
}
