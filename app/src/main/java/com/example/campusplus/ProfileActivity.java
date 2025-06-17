package com.example.campusplus;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImage, backButton;
    private Button editProfile, settings, logout, help;
    private TextView tvName, tvPersonalStatement;

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    // ActivityResultLaunchers for image pick and edit profile
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private ActivityResultLauncher<Intent> editProfileLauncher;
    private ActivityResultLauncher<Intent> settingsLauncher;

    private static final int SETTINGS_REQUEST_CODE = 101;

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
        tvPersonalStatement = findViewById(R.id.tv_personal_statement);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Initialize ActivityResultLaunchers

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        if (selectedImage != null) {
                            // Show image locally immediately
                            profileImage.setImageURI(selectedImage);
                            // Upload the image to Firebase Storage and update Firestore
                            uploadImageToFirebase(selectedImage);
                        }
                    }
                });

        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        boolean updated = result.getData().getBooleanExtra("updated", false);
                        if (updated) {
                            loadUserData();
                            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        settingsLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Refresh profile if returned from settings
                    loadUserData();
                });

        loadUserData();

        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });

        editProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            editProfileLauncher.launch(intent);
        });

        settings.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
            settingsLauncher.launch(intent);
        });

        help.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, DeveloperActivity.class);
            startActivity(intent);
        });

        logout.setOnClickListener(v -> showLogoutDialog());

        // Handle back press without using deprecated onBackPressed()
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish(); // or call whatever you want when back pressed
            }
        });
    }

    private void loadUserData() {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();

        firestore.collection("Users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String statement = documentSnapshot.getString("statement");
                        String profileImageUri = documentSnapshot.getString("profileImageUri");

                        tvName.setText(name != null ? name : "N/A");
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

    private void uploadImageToFirebase(Uri imageUri) {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference profileImagesRef = storageRef.child("profile_images/" + userId + ".jpg");

        profileImagesRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> profileImagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String downloadUrl = uri.toString();

                    firestore.collection("Users").document(userId)
                            .update("profileImageUri", downloadUrl)
                            .addOnSuccessListener(aVoid -> {
                                Picasso.get().load(downloadUrl).into(profileImage);
                                Toast.makeText(ProfileActivity.this, "Profile image updated!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(ProfileActivity.this, "Failed to save image URL", Toast.LENGTH_SHORT).show());
                }))
                .addOnFailureListener(e ->
                        Toast.makeText(ProfileActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show());
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
