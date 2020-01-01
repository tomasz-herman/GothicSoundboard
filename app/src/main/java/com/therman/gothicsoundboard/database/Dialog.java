package com.therman.gothicsoundboard.database;

public class Dialog {
    private String file;
    private Actor actor;
    private Character who;
    private Character from;
    private Character to;
    private String text;

    public Dialog(String file, Actor actor, Character who, Character from, Character to, String text) {
        this.file = file;
        this.actor = actor;
        this.who = who;
        this.from = from;
        this.to = to;
        this.text = text;
    }

    public String getFile() {
        return file;
    }

    public Actor getActor() {
        return actor;
    }

    public Character getWho() {
        return who;
    }

    public Character getFrom() {
        return from;
    }

    public Character getTo() {
        return to;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }
}
