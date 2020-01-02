package com.therman.gothicsoundboard.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Filterable;

import com.therman.gothicsoundboard.GothicSoundboard;
import com.therman.gothicsoundboard.R;
import com.therman.gothicsoundboard.database.Dialog;
import com.therman.gothicsoundboard.fragments.CharacterAdapter;
import com.therman.gothicsoundboard.fragments.DialogAdapter;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExploreActivity extends AppCompatActivity implements CharacterAdapter.ItemClicked {

    RecyclerView rvDialogs;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        rvDialogs = findViewById(R.id.rvDialogs);
        fragmentManager = getSupportFragmentManager();
        if(findViewById(R.id.layout_portrait) != null){
            fragmentManager.beginTransaction()
                    .show(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.fragCharacters)))
                    .hide(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.fragDialogs)))
                    .commit();
        }
        if(findViewById(R.id.layout_landscape) != null){
            fragmentManager.beginTransaction()
                    .show(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.fragCharacters)))
                    .show(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.fragDialogs)))
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem iSearch = menu.findItem(R.id.iSearch);
        SearchView searchView = (SearchView) iSearch.getActionView();
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

    @Override
    public void onItemClicked(int id) {
        ArrayList<Dialog> dialogs;
        if(id == 0)
            dialogs = GothicSoundboard.database.getDialogs().collect(Collectors.toCollection(ArrayList::new));
        else
            dialogs = GothicSoundboard.database.getDialogs().filter(dialog -> dialog.getWho().getId() == id).collect(Collectors.toCollection(ArrayList::new));
        ((DialogAdapter) Objects.requireNonNull(rvDialogs.getAdapter())).replaceData(dialogs);
        if(findViewById(R.id.layout_portrait) != null){
            fragmentManager.beginTransaction()
                    .hide(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.fragCharacters)))
                    .show(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.fragDialogs)))
                    .addToBackStack(null)
                    .commit();
        }
    }
}
