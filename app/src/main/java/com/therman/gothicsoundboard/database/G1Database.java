package com.therman.gothicsoundboard.database;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class G1Database {
    private List<Dialog> dialogs;
    private List<Actor> actors;
    private List<Character> characters;
    private List<CharacterActorPair> characterActorPairs;

    public G1Database(Context context){
        actors = loadActors(context);
        characters = loadCharacters(context);
        characterActorPairs = loadCharacterActorPairs(context);
        dialogs = loadDialogs(context);
    }

    private List<Actor> loadActors(Context context){
        List<Actor> actors = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("actors.txt")))){
            String line;
            reader.readLine();
            while((line = reader.readLine()) != null){
                if(line.matches("\\s*")) continue;
                String[] tokens = line.split(";");
                actors.add(
                        new Actor(
                                Integer.parseInt(tokens[0]),
                                tokens[1],
                                tokens[2]
                        )
                );
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
        return actors;
    }

    private List<Character> loadCharacters(Context context){
        List<Character> characters = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("characters.txt")))){
            String line;
            reader.readLine();
            while((line = reader.readLine()) != null){
                if(line.matches("\\s*")) continue;
                String[] tokens = line.split(";");
                characters.add(
                        new Character(
                                Integer.parseInt(tokens[0]),
                                tokens[1]
                        )
                );
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
        return characters;
    }

    private List<CharacterActorPair> loadCharacterActorPairs(Context context){
        List<CharacterActorPair> characterActorPairs = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("character-actor.txt")))){
            String line;
            reader.readLine();
            while((line = reader.readLine()) != null){
                if(line.matches("\\s*")) continue;
                String[] tokens = line.split(";");
                characterActorPairs.add(
                        new CharacterActorPair(
                                characters.get(Integer.parseInt(tokens[0])-1),
                                actors.get(Integer.parseInt(tokens[1])-1)
                        )
                );
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
        return characterActorPairs;
    }

    private List<Dialog> loadDialogs(Context context){
        List<Dialog> dialogs = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("dialogs.txt")))){
            String line;
            reader.readLine();
            while((line = reader.readLine()) != null){
                if(line.matches("\\s*")) continue;
                String[] tokens = line.split(";");
                dialogs.add(
                        new Dialog(
                                tokens[0],
                                actors.get(Integer.parseInt(tokens[1])-1),
                                characters.get(Integer.parseInt(tokens[2])-1),
                                characters.get(Integer.parseInt(tokens[3])-1),
                                characters.get(Integer.parseInt(tokens[4])-1),
                                tokens.length == 6 ? tokens[5] : ""
                        )
                );
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
        return dialogs;
    }

    public Stream<Dialog> getDialogs() {
        return dialogs.stream();
    }

    public Stream<Actor> getActors() {
        return actors.stream();
    }

    public Stream<Character> getCharacters() {
        return characters.stream();
    }

    public Stream<CharacterActorPair> getCharacterActorPairs() {
        return characterActorPairs.stream();
    }
}
