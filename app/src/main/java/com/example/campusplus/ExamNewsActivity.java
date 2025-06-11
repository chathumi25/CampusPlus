package com.example.campusplus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class ExamNewsActivity extends AppCompatActivity {

    ImageView backButton, profileIcon, likeIcon, commentIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_news);

        // Initialize views
        backButton = findViewById(R.id.backButton);
        profileIcon = findViewById(R.id.profileIcon);


        // Back button → finish activity
        if (backButton != null) {
            backButton.setOnClickListener(view -> finish());
        }

        // Profile icon → open ProfileActivity
        if (profileIcon != null) {
            profileIcon.setOnClickListener(view -> {
                Intent intent = new Intent(ExamNewsActivity.this, ProfileActivity.class);
                startActivity(intent);
            });
        }

        // Optional: Like button interaction
        if (likeIcon != null) {
            likeIcon.setOnClickListener(v -> {
                // TODO: Add like functionality
            });
        }

        // Optional: Comment button interaction
        if (commentIcon != null) {
            commentIcon.setOnClickListener(v -> {
                // TODO: Add comment functionality
            });
        }
    }
}
