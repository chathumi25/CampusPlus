package com.example.campusplus;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    ImageView profileImage, backButton;
    Button editProfile, settings, logout, help;
    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_main); // Keep original layout

        profileImage = findViewById(R.id.profile_image);
        backButton = findViewById(R.id.backButton);
        editProfile = findViewById(R.id.edit_profile);
        settings = findViewById(R.id.setting);
        logout = findViewById(R.id.logout);
        help = findViewById(R.id.help);

        if (profileImage != null) {
            profileImage.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            });
        }

        if (editProfile != null) {
            editProfile.setOnClickListener(v -> {
                // Already opens EditProfileActivity
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            });
        }

        if (settings != null) {
            settings.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                startActivity(intent);
            });
        }

        if (help != null) {
            help.setOnClickListener(v -> {
                // Changed from opening URL to opening DeveloperActivity screen
                Intent intent = new Intent(ProfileActivity.this, DeveloperActivity.class);
                startActivity(intent);
            });
        }

        if (logout != null) {
            logout.setOnClickListener(v -> showLogoutDialog());
        }

        if (backButton != null) {
            backButton.setOnClickListener(v -> onBackPressed());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            profileImage.setImageURI(selectedImage);
        }
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sign Out")
                .setMessage("Really want to sign out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Toast.makeText(ProfileActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
