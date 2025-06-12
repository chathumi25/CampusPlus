package com.example.campusplus;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DeveloperActivity extends AppCompatActivity {

    Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.developer_main);

        exitButton = findViewById(R.id.exit_button);

        exitButton.setOnClickListener(v -> showLogoutConfirmation());
    }

    private void showLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Log Out and Exit")
                .setMessage("Are you sure you want to log out and exit the app?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    logoutUser();  // clear user data
                    finishAffinity(); // close all activities
                    System.exit(0);   // exit app
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void logoutUser() {
        // Clear SharedPreferences or session data here
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear(); // or editor.remove("key") for specific logout data
        editor.apply();
    }
}

