package com.example.campusplus;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    ImageView profileImage, backButton;
    Button editProfile, settings, logout, help;
    TextView tvName, tvEmail, tvPersonalStatement;
    private static final int PICK_IMAGE = 1;

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_main);

        profileImage = findViewById(R.id.profile_image);
        backButton = findViewById(R.id.backButton);
        editProfile = findViewById(R.id.edit_profile);
        settings = findViewById(R.id.setting);
        logout = findViewById(R.id.logout);
        help = findViewById(R.id.help);

        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_personal_statement); // fixed: email textview should be tv_email
        tvPersonalStatement = findViewById(R.id.tv_personal_statement);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        loadUserData();

        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });

        editProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        settings.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        help.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, DeveloperActivity.class);
            startActivity(intent);
        });

        logout.setOnClickListener(v -> showLogoutDialog());
        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void loadUserData() {
        String userId = mAuth.getCurrentUser().getUid();

        firestore.collection("Users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String email = documentSnapshot.getString("email");
                        String statement = documentSnapshot.getString("statement");
                        String profileImageUri = documentSnapshot.getString("profileImageUri");

                        tvName.setText(name != null ? name : "N/A");
                        tvEmail.setText(email != null ? email : "N/A");
                        tvPersonalStatement.setText(statement != null ? statement : "No statement yet.");

                        if (profileImageUri != null && !profileImageUri.isEmpty()) {
                            Picasso.get().load(profileImageUri).into(profileImage);
                        } else {
                            profileImage.setImageResource(R.drawable.dp); // fallback default image
                        }
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show());
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
        new AlertDialog.Builder(this)
                .setTitle("Sign Out")
                .setMessage("Really want to sign out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Toast.makeText(ProfileActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
