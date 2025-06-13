package com.example.campusplus;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class DeveloperActivity extends AppCompatActivity {

    private Button exitButton;
    private ImageView backButton; // Declare back icon

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.developer_main);

        exitButton = findViewById(R.id.exit_button);
        backButton = findViewById(R.id.backButton); // Link to back icon in XML

        exitButton.setOnClickListener(v -> showLogoutConfirmation());

        backButton.setOnClickListener(v -> finish()); // Go back to previous screen
    }

    private void showLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Log Out and Exit")
                .setMessage("Are you sure you want to log out and exit the app?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    logoutUser();
                    finishAffinity();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void logoutUser() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
