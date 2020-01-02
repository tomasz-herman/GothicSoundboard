package com.therman.gothicsoundboard.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Filterable;

import com.therman.gothicsoundboard.GothicSoundboard;
import com.therman.gothicsoundboard.R;
import com.therman.gothicsoundboard.database.Dialog;
import com.therman.gothicsoundboard.fragments.DialogAdapter;

import java.util.ArrayList;
import java.util.Objects;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem iSearch = menu.findItem(R.id.iSearch);
        SearchView searchView = (SearchView) iSearch.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ((Filterable) Objects.requireNonNull(rvDialogs.getAdapter())).getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void search(){
        Stream<Dialog> stream = GothicSoundboard.database.getDialogs();
        if(getIntent().getBooleanExtra("favorites", false)){
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            stream = stream.filter(d -> preferences.contains(d.getFile()));
        } else {
            String actor = getIntent().getStringExtra("actor") != null ? getIntent().getStringExtra("actor").toLowerCase() : "";
            String character = getIntent().getStringExtra("character") != null ? getIntent().getStringExtra("character").toLowerCase() : "";
            String dialog = getIntent().getStringExtra("dialog") != null ? getIntent().getStringExtra("dialog").toLowerCase() : "";
            if(!actor.isEmpty()) stream = stream.filter(d -> d.getActor().toString().toLowerCase().contains(actor));
            if(!character.isEmpty()) stream = stream.filter(d -> d.getFrom().getName().toLowerCase().contains(character));
            if(!dialog.isEmpty()) stream = stream.filter(d -> d.getText().toLowerCase().contains(dialog.toLowerCase()));
        }
        ((DialogAdapter) Objects.requireNonNull(rvDialogs.getAdapter())).replaceData(stream.collect(Collectors.toCollection(ArrayList::new)));
    }
}
