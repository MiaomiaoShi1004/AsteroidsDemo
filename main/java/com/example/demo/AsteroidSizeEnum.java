package com.example.demo;

/**
 @Author Lyu Yongjie
 @Date 2020/12/20 16:44
 */

public enum AsteroidSizeEnum {
    SMALL(10.0),
    MEDIUM(20.0),
    LARGE(40.0);

    private final double value;

    private AsteroidSizeEnum(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
