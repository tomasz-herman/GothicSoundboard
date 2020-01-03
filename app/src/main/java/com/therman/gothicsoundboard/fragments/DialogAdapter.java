package com.therman.gothicsoundboard.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.therman.gothicsoundboard.R;
import com.therman.gothicsoundboard.database.Dialog;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.ViewHolder> implements Filterable {

    private ArrayList<Dialog> allDialogs;
    private ArrayList<Dialog> dialogs;
    private Context context;
    private MediaPlayer player = new MediaPlayer();

    public DialogAdapter(Context context, ArrayList<Dialog> dialogs) {
        this.dialogs = dialogs;
        this.allDialogs = new ArrayList<>(dialogs);
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
            itemView.setOnClickListener(this::playDialog);
            itemView.setOnLongClickListener(this::shareDialog);
            ivFavorite.setOnClickListener(this::setFavorite);
        }

        private void playDialog(View v) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            try {
                File filePath = new File(preferences.getString("directory", "") + File.separator + ((Dialog) v.getTag()).getFile());
                if (!filePath.exists()) {
                    Toast.makeText(context, "Missing file: " + ((Dialog) v.getTag()).getFile(), Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    if(player.isPlaying()) player.release();
                } catch (IllegalStateException e){
                    e.printStackTrace();
                }
                Log.d("Playing:", ((Dialog) v.getTag()).getFile());
                player = new MediaPlayer();
                player.setOnCompletionListener(MediaPlayer::release);
                FileInputStream inputStream = new FileInputStream(filePath);
                FileDescriptor fd = inputStream.getFD();
                player.setDataSource(fd);
                player.prepareAsync();
                player.setOnPreparedListener(MediaPlayer::start);
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void setFavorite(View v) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            if (prefs.contains((String) v.getTag())) {
                ivFavorite.setImageResource(R.drawable.unfavorite);
                prefs.edit().remove((String) v.getTag()).apply();
            } else {
                ivFavorite.setImageResource(R.drawable.favorite);
                prefs.edit().putBoolean((String) v.getTag(), true).apply();
            }
        }

        private boolean shareDialog(View v) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            File file = new File(preferences.getString("directory", "") + File.separator + ((Dialog) v.getTag()).getFile());
            if (!file.exists()) {
                Toast.makeText(context, "Missing file: " + ((Dialog) v.getTag()).getFile(), Toast.LENGTH_SHORT).show();
                return true;
            }
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file.getAbsolutePath()));
            shareIntent.setType(URLConnection.guessContentTypeFromName(file.getName()));
            context.startActivity(Intent.createChooser(shareIntent, "Share a file: " + ((Dialog) v.getTag()).getFile()));
            return true;
        }
    }
    public void replaceData(ArrayList<Dialog> dialogs){
        this.dialogs = dialogs;
        this.allDialogs = new ArrayList<>(dialogs);
        notifyDataSetChanged();
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Dialog> filteredDialogs;
                if(constraint == null || constraint.length() == 0)
                    filteredDialogs = new ArrayList<>(allDialogs);
                else {
                    String pattern = constraint.toString().toLowerCase().trim();
                    filteredDialogs = allDialogs.stream().filter(
                            dialog -> dialog.getText().toLowerCase().contains(pattern)
                                            || dialog.getFrom().toString().toLowerCase().contains(pattern)
                                            || dialog.getTo().toString().toLowerCase().contains(pattern)
                                            || dialog.getActor().toString().toLowerCase().contains(pattern)
                    ).collect(Collectors.toCollection(ArrayList::new));
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredDialogs;
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dialogs = (ArrayList<Dialog>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
