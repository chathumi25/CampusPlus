package com.example.campusplus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    ImageView backIcon;
    Button btnLogin, btnProfile, btnNews, btnAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_main); // your XML file name

        // Find views
        backIcon = findViewById(R.id.backicon); // you'll need to set this ID
        btnLogin = findViewById(R.id.btn_login);
        btnProfile = findViewById(R.id.btn_profile);
        btnNews = findViewById(R.id.btn_news);
        btnAbout = findViewById(R.id.btn_about);

        // Back icon click: finish this activity
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // goes back to previous activity
            }
        });

        // Login button click
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
            }
        });

        // Profile button click
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
            }
        });

        // News button click
        btnNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, NewsMainActivity.class));
            }
        });

        // Developer Info button click
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, DeveloperActivity.class));
            }
        });
    }
}
