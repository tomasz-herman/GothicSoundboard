package com.therman.gothicsoundboard.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.therman.gothicsoundboard.GothicSoundboard;
import com.therman.gothicsoundboard.R;
import com.therman.gothicsoundboard.database.Dialog;
import com.therman.gothicsoundboard.fragments.DialogAdapter;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SearchResultActivity extends AppCompatActivity {

    RecyclerView rvDialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        rvDialogs = findViewById(R.id.rvDialogs);
    }

    @Override
    protected void onStart() {
        super.onStart();
        search();
    }

    private void search(){
        String actor = getIntent().getStringExtra("actor") != null ? getIntent().getStringExtra("actor").toLowerCase() : "";
        String character = getIntent().getStringExtra("character") != null ? getIntent().getStringExtra("character").toLowerCase() : "";
        String dialog = getIntent().getStringExtra("dialog") != null ? getIntent().getStringExtra("dialog").toLowerCase() : "";
        Stream<Dialog> stream = GothicSoundboard.database.getDialogs();
        if(!actor.isEmpty()) stream = stream.filter(d -> d.getActor().toString().toLowerCase().contains(actor));
        if(!character.isEmpty()) stream = stream.filter(d -> d.getFrom().getName().toLowerCase().contains(character));
        if(!dialog.isEmpty()) stream = stream.filter(d -> d.getText().toLowerCase().contains(dialog.toLowerCase()));
        rvDialogs.setAdapter(new DialogAdapter(this, stream.collect(Collectors.toCollection(ArrayList::new))));
    }
}
