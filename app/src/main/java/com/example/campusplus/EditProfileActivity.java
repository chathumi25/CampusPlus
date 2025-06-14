package com.example.campusplus;

import android.app.Activity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit_main);

        profileImage = findViewById(R.id.profile_image);
        editIcon = findViewById(R.id.edit_profile_icon);
        backButton = findViewById(R.id.backButton);
        nameEditText = findViewById(R.id.edit_name);
        emailEditText = findViewById(R.id.edit_email);
        statementEditText = findViewById(R.id.edit_statement);
        saveButton = findViewById(R.id.save_button);

        // Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        uid = auth.getCurrentUser().getUid();

        // Load user data
        loadUserData();

        editIcon.setOnClickListener(view -> openImagePicker());
        saveButton.setOnClickListener(view -> saveProfile());
        backButton.setOnClickListener(v -> finish());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), PICK_IMAGE_REQUEST);
    }

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
                    profileImage.setImageResource(R.drawable.dp);
                }
            }
        }).addOnFailureListener(e ->
                Toast.makeText(EditProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show());
    }

    private void saveProfile() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String statement = statementEditText.getText().toString().trim();

        if (selectedImageUri != null) {
            FirebaseStorage.getInstance().getReference("profileImages/" + uid)
                    .putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();

                            Map<String, Object> userUpdates = new HashMap<>();
                            userUpdates.put("name", name);
                            userUpdates.put("email", email);
                            userUpdates.put("statement", statement);
                            userUpdates.put("profileImageUri", imageUrl);

                            db.collection("users").document(uid).update(userUpdates)
                                    .addOnSuccessListener(unused ->
                                            Toast.makeText(EditProfileActivity.this, "Profile updated!", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e ->
                                            Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show());
                        });
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(EditProfileActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show());
        } else {
            Map<String, Object> userUpdates = new HashMap<>();
            userUpdates.put("name", name);
            userUpdates.put("email", email);
            userUpdates.put("statement", statement);

            db.collection("users").document(uid).update(userUpdates)
                    .addOnSuccessListener(unused ->
                            Toast.makeText(EditProfileActivity.this, "Profile updated!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show());
        }
    }
}
