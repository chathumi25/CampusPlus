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

    ImageView profileImage, editIcon;
    EditText nameEditText, emailEditText, statementEditText;
    Button saveButton;

    Uri selectedImageUri; // to track the selected image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit_main);

        profileImage = findViewById(R.id.profile_image);
        editIcon = findViewById(R.id.edit_profile_icon);
        nameEditText = findViewById(R.id.edit_name);
        emailEditText = findViewById(R.id.edit_email);
        statementEditText = findViewById(R.id.edit_statement);
        saveButton = findViewById(R.id.save_button);

        // Preload default profile (you can use Glide or Picasso if using remote URLs)
        profileImage.setImageResource(R.drawable.dp); // local drawable

        // 1. Change profile picture
        editIcon.setOnClickListener(view -> openImagePicker());

        // 2. Save edited details
        saveButton.setOnClickListener(view -> saveProfile());
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
        String emails = emailEditText.getText().toString().trim(); // allow multiple
        String statement = statementEditText.getText().toString().trim();

        // You can store the values in SharedPreferences, database, or API call
        Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show();

        // Example log (in real app, you'd save or send these values)
        System.out.println("Name: " + name);
        System.out.println("Email(s): " + emails);
        System.out.println("Statement: " + statement);
        System.out.println("Image URI: " + (selectedImageUri != null ? selectedImageUri.toString() : "No change"));
    }
}

