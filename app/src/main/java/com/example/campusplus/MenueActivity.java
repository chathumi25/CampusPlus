package com.example.campusplus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MenueActivity extends AppCompatActivity {

    ImageView backArrow;
    TextView menuProfile, menuSettings, menuNews, menuLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);

        backArrow = findViewById(R.id.backArrow);
        menuProfile = findViewById(R.id.menu_profile);
        menuSettings = findViewById(R.id.menu_settings);
        menuNews = findViewById(R.id.menu_news);
        menuLogout = findViewById(R.id.menu_home); // Logout is mistakenly given ID "menu_home"

        // Back Arrow
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(); // Goes back to the previous activity
            }
        });

        // Profile screen
        menuProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenueActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // Settings screen
        menuSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenueActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        // News screen
        menuNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenueActivity.this, NewsMainActivity.class);
                startActivity(intent);
            }
        });

        // Logout confirmation dialog
        menuLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutDialog();
            }
        });
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(MenueActivity.this)
                .setTitle("Logout")
                .setMessage("Do you really want to log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity(); // Close all activities and exit the app
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
