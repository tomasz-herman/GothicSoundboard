package com.therman.gothicsoundboard.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.therman.gothicsoundboard.R;
import com.therman.gothicsoundboard.database.Dialog;

import java.util.ArrayList;

public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.ViewHolder> {

    private ArrayList<Dialog> dialogs;
    private Context context;

    public DialogAdapter(Context context, ArrayList<Dialog> dialogs) {
        this.dialogs = dialogs;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDialogFrom, tvDialogText;
        ImageView ivFavorite;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDialogFrom = itemView.findViewById(R.id.tvDialogFrom);
            tvDialogText = itemView.findViewById(R.id.tvDialogText);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
            itemView.setOnClickListener(v -> Toast.makeText(context, "Playing: " + ((Dialog)v.getTag()).getFile(), Toast.LENGTH_SHORT).show());
            ivFavorite.setOnClickListener(v -> {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                if(prefs.contains((String)v.getTag())){
                    ivFavorite.setImageResource(R.drawable.unfavorite);
                    prefs.edit().remove((String)v.getTag()).apply();
                } else {
                    ivFavorite.setImageResource(R.drawable.favorite);
                    prefs.edit().putBoolean((String)v.getTag(), true).apply();
                }
            });
        }
    }

    @NonNull
    @Override
    public DialogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_layout, parent, false);
        return new DialogAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DialogAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(dialogs.get(position));
        holder.ivFavorite.setTag(dialogs.get(position).getFile());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if(prefs.contains(dialogs.get(position).getFile()))
            holder.ivFavorite.setImageResource(R.drawable.favorite);
        else holder.ivFavorite.setImageResource(R.drawable.unfavorite);
        holder.tvDialogFrom.setText(dialogs.get(position).getFrom().getName());
        holder.tvDialogText.setText(dialogs.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return dialogs.size();
    }
}
