package com.therman.gothicsoundboard.fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.therman.gothicsoundboard.GothicSoundboard;
import com.therman.gothicsoundboard.R;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 */
public class CharactersFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    View view;

    public CharactersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_characters, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = view.findViewById(R.id.rvCharacters);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CharacterAdapter(getActivity(), GothicSoundboard.database.getCharacters().collect(Collectors.toCollection(ArrayList::new)));
        recyclerView.setAdapter(adapter);
    }
}
