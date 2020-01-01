package com.therman.gothicsoundboard;

import android.app.Application;

import com.therman.gothicsoundboard.database.G1Database;

public class GothicSoundboard extends Application {

    public static G1Database database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = new G1Database(this);
    }
}
