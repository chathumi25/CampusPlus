package com.example.campusplus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    ImageView profileImage, editIcon, backButton;
    EditText nameEditText, emailEditText, statementEditText;
    Button saveButton;

    Uri selectedImageUri;
    FirebaseFirestore db;
    FirebaseAuth auth;
    String uid;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit_main);

        // Initialize views
        profileImage = findViewById(R.id.profile_image);
        editIcon = findViewById(R.id.edit_profile_icon);
        backButton = findViewById(R.id.backButton);
        nameEditText = findViewById(R.id.edit_name);
        emailEditText = findViewById(R.id.edit_email);
        statementEditText = findViewById(R.id.edit_statement);
        saveButton = findViewById(R.id.save_button);

        // Prevent editing email
        emailEditText.setEnabled(false);

        // Firebase instances
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        uid = auth.getCurrentUser().getUid();

        // Load current user profile data
        loadUserData();

        // Open image picker when edit icon clicked
        editIcon.setOnClickListener(view -> openImagePicker());

        // Save profile info on button click
        saveButton.setOnClickListener(view -> saveProfile());

        // Go back to previous screen
        backButton.setOnClickListener(v -> finish());
    }

    // Open image picker
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), PICK_IMAGE_REQUEST);
    }

    // Handle result from image picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Load profile info from Firestore
    private void loadUserData() {
        DocumentReference userRef = db.collection("Users").document(uid);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String name = documentSnapshot.getString("name");
                String email = documentSnapshot.getString("email");
                String statement = documentSnapshot.getString("statement");
                String imageUri = documentSnapshot.getString("profileImageUri");

                nameEditText.setText(name != null ? name : "");
                emailEditText.setText(email != null ? email : "");
                statementEditText.setText(statement != null ? statement : "");

                if (imageUri != null && !imageUri.isEmpty()) {
                    Picasso.get().load(imageUri).into(profileImage);
                } else {
                    profileImage.setImageResource(R.drawable.dp); // default image
                }
            } else {
                Toast.makeText(this, "No user data found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Failed to load profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Save profile updates to Firestore
    private void saveProfile() {
        String name = nameEditText.getText().toString().trim();
        String statement = statementEditText.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating profile...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("name", name);
        userUpdates.put("statement", statement);

        DocumentReference userDocRef = db.collection("Users").document(uid);

        if (selectedImageUri != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference("profileImages/" + uid);
            storageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot ->
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                                userUpdates.put("profileImageUri", uri.toString());

                                // Update Firestore
                                updateFirestore(userDocRef, userUpdates);
                            }))
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(EditProfileActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                    });
        } else {
            updateFirestore(userDocRef, userUpdates);
        }
    }

    private void updateFirestore(DocumentReference userDocRef, Map<String, Object> userUpdates) {
        userDocRef.update(userUpdates)
                .addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Profile updated!", Toast.LENGTH_SHORT).show();

                    // Send result to refresh ProfileActivity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updated", true);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                });
    }
}
