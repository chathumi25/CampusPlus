package com.example.campusplus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SportsNewsActivity extends AppCompatActivity {

    private ImageView backButton, profileIcon, likeIcon, commentIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_news);

        // View Bindings
        backButton = findViewById(R.id.backButton);
        profileIcon = findViewById(R.id.profileIcon);
           // Optional

        // Go back to NewsMainActivity
        if (backButton != null) {
            backButton.setOnClickListener(view -> finish());
        }

        // Go to ProfileActivity
        if (profileIcon != null) {
            profileIcon.setOnClickListener(view -> {
                Intent intent = new Intent(SportsNewsActivity.this, ProfileActivity.class);
                startActivity(intent);
            });
        }

        // Like button interaction (optional)
        if (likeIcon != null) {
            likeIcon.setOnClickListener(v -> {
                // TODO: Add like logic
            });
        }

        // Comment button interaction (optional)
        if (commentIcon != null) {
            commentIcon.setOnClickListener(v -> {
                // TODO: Add comment logic
            });
        }
    }
}

