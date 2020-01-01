package com.therman.gothicsoundboard.database;

public class Character {
    private int id;
    private String name;

    public Character(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
