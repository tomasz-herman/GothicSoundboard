package com.therman.gothicsoundboard.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.Toast;

import com.therman.gothicsoundboard.GothicSoundboard;
import com.therman.gothicsoundboard.R;
import com.therman.gothicsoundboard.database.Dialog;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SmalltalkActivity extends AppCompatActivity {

    private static final int ACTORS = 14;
    TextSwitcher tsSmalltalkTop, tsSmalltalkBottom;
    MediaPlayer player;
    MediaPlayer silencePlayer;
    List<ArrayList<Dialog>> smalltalks;
    HandlerThread smalltalkThread = new HandlerThread("SmalltalkThread");
    Handler threadHandler;
    ArrayDeque<Integer> actors;
    ArrayDeque<Dialog> topQueue, bottomQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smalltalk);
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        tsSmalltalkBottom = findViewById(R.id.tsSmalltalkBottom);
        tsSmalltalkTop = findViewById(R.id.tsSmalltalkTop);
        tsSmalltalkBottom.setInAnimation(in);
        tsSmalltalkBottom.setOutAnimation(out);
        tsSmalltalkTop.setInAnimation(in);
        tsSmalltalkTop.setOutAnimation(out);
        smalltalks = new ArrayList<>();
        actors = new ArrayDeque<>();
        topQueue = new ArrayDeque<>();
        bottomQueue = new ArrayDeque<>();
        for (int i = 0; i < ACTORS; i++) smalltalks.add(new ArrayList<>());
        GothicSoundboard.database.getDialogs()
                .filter(dialog -> dialog.getWho().getName().equals("Rozmowa"))
                .collect(Collectors.toCollection(ArrayList::new))
                .forEach(dialog -> smalltalks.get(dialog.getActor().getId() - 1).add(dialog));
        smalltalkThread.start();
        Looper looper = smalltalkThread.getLooper();
        threadHandler = new Handler(looper);
        threadHandler.post(new SmalltalkTopRunnable());
        loopSilence();
    }

    private void loopSilence(){
        try {
            AssetFileDescriptor afd = getAssets().openFd("void.mp3");
            silencePlayer = new MediaPlayer();
            silencePlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            silencePlayer.setLooping(true);
            silencePlayer.prepare();
            silencePlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Dialog getDialog(ArrayDeque<Dialog> dialogQueue){
        if(!dialogQueue.isEmpty()) return dialogQueue.removeFirst();
        Random random = new Random();
        if(actors.isEmpty()) {
            ArrayList<Integer> ints = IntStream.range(1, ACTORS).boxed().collect(Collectors.toCollection(ArrayList::new));
            while (!ints.isEmpty()) {
                int pos = random.nextInt(ints.size());
                actors.push(ints.get(pos));
                ints.remove(pos);
            }
        }
        int actor = actors.removeFirst();
        ArrayList<Dialog> dialogs = new ArrayList<>(smalltalks.get(actor));
        while (!dialogs.isEmpty()) {
            int pos = random.nextInt(dialogs.size());
            dialogQueue.push(dialogs.get(pos));
            dialogs.remove(pos);
        }
        return getDialog(dialogQueue);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        smalltalkThread.quitSafely();
        silencePlayer.release();
    }

    private class SmalltalkTopRunnable implements Runnable {
        @Override
        public void run() {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SmalltalkActivity.this);
            SystemClock.sleep(Integer.parseInt(Objects.requireNonNull(preferences.getString("smalltalk_interval", "0"))));
            try {
                Dialog dialog = getDialog(topQueue);
                File filePath = new File(preferences.getString("directory", "") + File.separator + dialog.getFile());
                if (!filePath.exists()) {
                    Toast.makeText(SmalltalkActivity.this, "Missing file: " + dialog.getFile(), Toast.LENGTH_SHORT).show();
                    return;
                }
                player = new MediaPlayer();
                tsSmalltalkTop.post(() -> tsSmalltalkTop.setText(dialog.getText()));
                player.setOnCompletionListener(mediaPlayer -> {
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    threadHandler.post(new SmalltalkBottomRunnable());
                    if(preferences.getBoolean("smalltalk_fade", true)){
                        int sleep = Integer.parseInt(Objects.requireNonNull(preferences.getString("smalltalk_fade_delay", "0")));
                        tsSmalltalkTop.postDelayed(() -> tsSmalltalkTop.setText(""), sleep);
                    }
                });
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
    }

    private class SmalltalkBottomRunnable implements Runnable {
        @Override
        public void run() {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SmalltalkActivity.this);
            SystemClock.sleep(Integer.parseInt(Objects.requireNonNull(preferences.getString("smalltalk_interval", "0"))));
            try {
                Dialog dialog = getDialog(bottomQueue);
                File filePath = new File(preferences.getString("directory", "") + File.separator + dialog.getFile());
                if (!filePath.exists()) {
                    Toast.makeText(SmalltalkActivity.this, "Missing file: " + dialog.getFile(), Toast.LENGTH_SHORT).show();
                    return;
                }
                player = new MediaPlayer();
                tsSmalltalkBottom.post(() -> tsSmalltalkBottom.setText(dialog.getText()));
                player.setOnCompletionListener(mediaPlayer -> {
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    threadHandler.post(new SmalltalkTopRunnable());
                    if(preferences.getBoolean("smalltalk_fade", true)){
                        int sleep = Integer.parseInt(Objects.requireNonNull(preferences.getString("smalltalk_fade_delay", "0")));
                        tsSmalltalkBottom.postDelayed(() -> tsSmalltalkBottom.setText(""), sleep);
                    }
                });
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
    }
}
