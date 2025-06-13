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

import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    ImageView profileImage, editIcon, backButton;
    EditText nameEditText, emailEditText, statementEditText;
    Button saveButton;

    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit_main);

        profileImage = findViewById(R.id.profile_image);
        editIcon = findViewById(R.id.edit_profile_icon);
        backButton = findViewById(R.id.backButton); // <-- Get the back button view
        nameEditText = findViewById(R.id.edit_name);
        emailEditText = findViewById(R.id.edit_email);
        statementEditText = findViewById(R.id.edit_statement);
        saveButton = findViewById(R.id.save_button);

        profileImage.setImageResource(R.drawable.dp);

        editIcon.setOnClickListener(view -> openImagePicker());
        saveButton.setOnClickListener(view -> saveProfile());

        // Back button click: go to previous screen
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

    private void saveProfile() {
        String name = nameEditText.getText().toString().trim();
        String emails = emailEditText.getText().toString().trim();
        String statement = statementEditText.getText().toString().trim();

        Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show();

        System.out.println("Name: " + name);
        System.out.println("Email(s): " + emails);
        System.out.println("Statement: " + statement);
        System.out.println("Image URI: " + (selectedImageUri != null ? selectedImageUri.toString() : "No change"));
    }
}
