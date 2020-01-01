package com.therman.gothicsoundboard.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.therman.gothicsoundboard.R;


public class MainActivity extends AppCompatActivity {

    Button btnExplore, btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnExplore = findViewById(R.id.btnExploreActivity);
        btnSearch = findViewById(R.id.btnSearchActivity);
        btnExplore.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ExploreActivity.class);
            startActivity(intent);
        });
        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });
    }
}
