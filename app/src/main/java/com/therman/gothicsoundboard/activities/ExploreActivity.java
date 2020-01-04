package com.therman.gothicsoundboard.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
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
    MenuItem iSearch;
    static int lastCharacterId = 0;
    static boolean shownDialogsFragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        fragmentManager = getSupportFragmentManager();
        rvDialogs = findViewById(R.id.rvDialogs);
        adjustFragments();
    }


    @Override
    protected void onStart() {
        super.onStart();
        filterDialogs(lastCharacterId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        iSearch = menu.findItem(R.id.iSearch);
        if(isPortraitMode() && !shownDialogsFragment) iSearch.setVisible(false);
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

    @Override
    public void onItemClicked(int id) {
        filterDialogs(lastCharacterId = id);
        shownDialogsFragment = true;
        if(findViewById(R.id.layout_portrait) != null) hideCharactersShowDialogs();
    }

    @Override
    public void onBackPressed() {
        if(isLandscapeMode() || !shownDialogsFragment){
            lastCharacterId = 0;
            shownDialogsFragment = false;
            super.onBackPressed();
        } else {
            shownDialogsFragment = false;
            showCharactersHideDialogs();
        }
    }

    private void filterDialogs(int id){
        ArrayList<Dialog> dialogs = id == 0 ?
                GothicSoundboard.database.getDialogs().collect(Collectors.toCollection(ArrayList::new)) :
                GothicSoundboard.database.getDialogs().filter(dialog -> dialog.getWho().getId() == id).collect(Collectors.toCollection(ArrayList::new));
        ((DialogAdapter) Objects.requireNonNull(rvDialogs.getAdapter())).replaceData(dialogs);
    }

    private void adjustFragments(){
        if(isPortraitMode())
            if (shownDialogsFragment) hideCharactersShowDialogs();
            else showCharactersHideDialogs();
        if(isLandscapeMode()) showCharactersAndDialogs();
    }

    private boolean isPortraitMode(){
        return findViewById(R.id.layout_portrait) != null;
    }

    private boolean isLandscapeMode(){
        return findViewById(R.id.layout_landscape) != null;
    }

    private void hideCharactersShowDialogs(){
        fragmentManager.beginTransaction()
                .hide(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.fragCharacters)))
                .show(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.fragDialogs)))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
        if(iSearch != null) iSearch.setVisible(true);

    }

    private void showCharactersHideDialogs(){
        fragmentManager.beginTransaction()
                .show(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.fragCharacters)))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .hide(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.fragDialogs)))
                .commit();
        if(iSearch != null) iSearch.setVisible(false);

    }

    private void showCharactersAndDialogs(){
        fragmentManager.beginTransaction()
                .show(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.fragCharacters)))
                .show(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.fragDialogs)))
                .commit();
        if(iSearch != null) iSearch.setVisible(true);
    }
}
