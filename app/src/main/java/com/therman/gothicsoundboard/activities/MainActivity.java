package com.therman.gothicsoundboard.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.therman.gothicsoundboard.R;


public class MainActivity extends AppCompatActivity {

    Button btnExplore, btnSearch, btnFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnExplore = findViewById(R.id.btnExploreActivity);
        btnSearch = findViewById(R.id.btnSearchActivity);
        btnFavorites = findViewById(R.id.btnFavorites);
        btnExplore.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ExploreActivity.class);
            startActivity(intent);
        });
        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });
        btnFavorites.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
            intent.putExtra("favorites", true);
            startActivity(intent);
        });
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
}
