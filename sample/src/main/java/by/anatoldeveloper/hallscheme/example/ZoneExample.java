package by.anatoldeveloper.hallscheme.example;

import by.anatoldeveloper.hallscheme.hall.Zone;

/**
 * Created by Nublo on 08.01.2016.
 * Copyright Nublo
 */
public class ZoneExample implements Zone {

    public int id;
    public int leftTopX;
    public int leftTopY;
    public int width;
    public int height;
    public int color;
    public String name;

    public ZoneExample() {

    }

    public ZoneExample(int id, int leftTopX, int leftTopY, int width, int height, int color, String name) {
        this.id = id;
        this.leftTopX = leftTopX;
        this.leftTopY = leftTopY;
        this.width = width;
        this.height = height;
        this.color = color;
        this.name = name;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public int leftTopX() {
        return leftTopX;
    }

    @Override
    public int leftTopY() {
        return leftTopY;
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public int color() {
        return color;
    }

    @Override
    public String name() {
        return name;
    }

}