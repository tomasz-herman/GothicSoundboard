package com.therman.gothicsoundboard.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.therman.gothicsoundboard.GothicSoundboard;
import com.therman.gothicsoundboard.R;
import com.therman.gothicsoundboard.database.Actor;
import com.therman.gothicsoundboard.database.Character;

import java.util.stream.Collectors;

public class SearchActivity extends AppCompatActivity {

    Button btnSearch;
    AutoCompleteTextView etCharacterName, etActorName;
    TextInputEditText etDialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        btnSearch = findViewById(R.id.btnSearch);
        etActorName = findViewById(R.id.etActorName);
        etCharacterName = findViewById(R.id.etCharacterName);
        etDialogText = findViewById(R.id.etDialogText);
        ArrayAdapter<String> actors = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,  GothicSoundboard.database.getActors().map(Actor::toString).collect(Collectors.toList()));
        ArrayAdapter<String> characters = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,  GothicSoundboard.database.getCharacters().map(Character::toString).collect(Collectors.toList()));
        etActorName.setAdapter(actors);
        etCharacterName.setAdapter(characters);
        etActorName.setThreshold(1);
        etCharacterName.setThreshold(1);
        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
            intent.putExtra("actor", etActorName.getText() != null ? etActorName.getText().toString() : "");
            intent.putExtra("character", etCharacterName.getText() != null ? etCharacterName.getText().toString() : "");
            intent.putExtra("dialog", etDialogText.getText() != null ? etDialogText.getText().toString() : "");
            startActivity(intent);
        });
    }
}
