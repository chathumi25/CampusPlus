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

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NewsMainActivity extends AppCompatActivity {

    private EditText searchBar;
    private ImageView profileIcon, menuIcon;
    private TextView sportsNewsCard, examNewsCard, academicNewsCard;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_main);

        // Initialize views
        profileIcon = findViewById(R.id.profile_icon);
        menuIcon = findViewById(R.id.menu_icon); // menu icon added here
        searchBar = findViewById(R.id.searchBar);
        sportsNewsCard = findViewById(R.id.sports_card);
        examNewsCard = findViewById(R.id.exam_card);
        academicNewsCard = findViewById(R.id.academic_card);
        btnLogout = findViewById(R.id.btnLogout);

        // Menu icon click - open menu screen (MenuActivity)
        menuIcon.setOnClickListener(v -> {
            Intent intent = new Intent(NewsMainActivity.this, MenueActivity.class);
            startActivity(intent);
        });

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

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_sports) {
                startActivity(new Intent(NewsMainActivity.this, SportsNewsActivity.class));
                return true;
            } else if (id == R.id.nav_academic) {
                startActivity(new Intent(NewsMainActivity.this, FacultyNewsActivity.class));
                return true;
            } else if (id == R.id.nav_exam) {
                startActivity(new Intent(NewsMainActivity.this, ExamNewsActivity.class));
                return true;
            }

            return false;
        });

    }
}
