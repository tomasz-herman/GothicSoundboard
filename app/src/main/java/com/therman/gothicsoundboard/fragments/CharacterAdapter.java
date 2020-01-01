package com.therman.gothicsoundboard.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.therman.gothicsoundboard.R;
import com.therman.gothicsoundboard.database.Character;

import java.util.ArrayList;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.ViewHolder> {

    private ArrayList<Character> characters;
    private ItemClicked activity;

    public interface ItemClicked {
        void onItemClicked(int id);
    }

    public CharacterAdapter(Context context, ArrayList<Character> characters) {
        this.characters = characters;
        this.characters.add(0, new Character(0, "All"));
        this.activity = (ItemClicked) context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCharacterName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCharacterName = itemView.findViewById(R.id.tvCharacterName);
            itemView.setOnClickListener(v -> activity.onItemClicked(((Character)v.getTag()).getId()));
        }
    }

    @NonNull
    @Override
    public CharacterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.character_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(characters.get(position));
        holder.tvCharacterName.setText(characters.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }
}
