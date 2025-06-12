package com.example.campusplus;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DeveloperActivity extends AppCompatActivity {

    private Button exitButton;

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
                    logoutUser();      // Clear user session data
                    finishAffinity();  // Close all activities and exit app

                    // System.exit(0); // Optional: can be used but not always recommended
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void logoutUser() {
        // Clear SharedPreferences or session data here
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear(); // Clear all stored preferences, you can selectively remove keys instead
        editor.apply();
    }
}

