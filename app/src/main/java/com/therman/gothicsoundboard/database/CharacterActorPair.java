package com.therman.gothicsoundboard.database;

public class CharacterActorPair {
    private Character character;
    private Actor actor;

    public CharacterActorPair(Character character, Actor actor) {
        this.character = character;
        this.actor = actor;
    }

    public Character getCharacter() {
        return character;
    }

    public Actor getActor() {
        return actor;
    }

    @Override
    public String toString() {
        return character + " : " + actor;
    }
}
