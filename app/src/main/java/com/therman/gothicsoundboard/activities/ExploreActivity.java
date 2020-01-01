package com.therman.gothicsoundboard.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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
    public void onItemClicked(int id) {
        ArrayList<Dialog> dialogs;
        if(id == 0)
            dialogs = GothicSoundboard.database.getDialogs().collect(Collectors.toCollection(ArrayList::new));
        else
            dialogs = GothicSoundboard.database.getDialogs().filter(dialog -> dialog.getWho().getId() == id).collect(Collectors.toCollection(ArrayList::new));
        rvDialogs.setAdapter(new DialogAdapter(this, dialogs));
        if(findViewById(R.id.layout_portrait) != null){
            fragmentManager.beginTransaction()
                    .hide(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.fragCharacters)))
                    .show(Objects.requireNonNull(fragmentManager.findFragmentById(R.id.fragDialogs)))
                    .addToBackStack(null)
                    .commit();
        }
    }
}
