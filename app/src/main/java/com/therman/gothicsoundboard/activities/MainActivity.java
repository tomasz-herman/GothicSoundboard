package com.therman.gothicsoundboard.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.therman.gothicsoundboard.R;


public class MainActivity extends AppCompatActivity {

    Button btnExplore, btnSearch, btnFavorites, btnSmalltalk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnExplore = findViewById(R.id.btnExploreActivity);
        btnSearch = findViewById(R.id.btnSearchActivity);
        btnFavorites = findViewById(R.id.btnFavorites);
        btnSmalltalk = findViewById(R.id.btnSmalltalk);
        btnExplore.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ExploreActivity.class);
            startActivity(intent);
        });
        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FilterActivity.class);
            startActivity(intent);
        });
        btnFavorites.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
            intent.putExtra("favorites", true);
            startActivity(intent);
        });
        btnSmalltalk.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SmalltalkActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForPermission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.iSettings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.iAbout:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkForPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        }
    }
}
