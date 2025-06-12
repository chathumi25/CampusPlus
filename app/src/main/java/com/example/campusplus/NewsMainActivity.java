package com.example.campusplus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NewsMainActivity extends AppCompatActivity {

    private EditText searchBar;
    private ImageView profileIcon;
    private TextView sportsNewsCard, examNewsCard, academicNewsCard;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_main);

        // Initialize views
        profileIcon = findViewById(R.id.profile_icon);
        searchBar = findViewById(R.id.searchBar);
        sportsNewsCard = findViewById(R.id.sports_card);
        examNewsCard = findViewById(R.id.exam_card);
        academicNewsCard = findViewById(R.id.academic_card);
        btnLogout = findViewById(R.id.btnLogout); // NEW

        // Open profile
        profileIcon.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });

        // Card listeners
        sportsNewsCard.setOnClickListener(v -> {
            startActivity(new Intent(this, SportsNewsActivity.class));
        });

        examNewsCard.setOnClickListener(v -> {
            startActivity(new Intent(this, ExamNewsActivity.class));
        });

        academicNewsCard.setOnClickListener(v -> {
            startActivity(new Intent(this, FacultyNewsActivity.class));
        });

        // LOGOUT Logic
        btnLogout.setOnClickListener(v -> {
            SharedPreferences preferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
            preferences.edit().clear().apply(); // Clear saved login
            Intent intent = new Intent(NewsMainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
            startActivity(intent);
        });
    }
}
