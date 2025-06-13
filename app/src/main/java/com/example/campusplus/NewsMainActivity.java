package com.example.campusplus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NewsMainActivity extends AppCompatActivity {

    private EditText searchBar;
    private ImageView profileIcon, menuIcon;
    private TextView sportsNewsCard, examNewsCard, academicNewsCard;
    private Button btnLogout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_main);

        // Initialize views safely
        profileIcon = findViewById(R.id.profile_icon);
        menuIcon = findViewById(R.id.menu_icon);
        searchBar = findViewById(R.id.searchBar);
        sportsNewsCard = findViewById(R.id.sports_card);
        examNewsCard = findViewById(R.id.exam_card);
        academicNewsCard = findViewById(R.id.academic_card);


        // Menu icon click: open menu activity
        if (menuIcon != null) {
            menuIcon.setOnClickListener(v -> {
                Intent intent = new Intent(NewsMainActivity.this, MenueActivity.class);
                startActivity(intent);
            });
        }

        // Profile icon click: open profile activity
        if (profileIcon != null) {
            profileIcon.setOnClickListener(v -> {
                startActivity(new Intent(NewsMainActivity.this, ProfileActivity.class));
            });
        }

        // Sports news card click
        if (sportsNewsCard != null) {
            sportsNewsCard.setOnClickListener(v -> {
                startActivity(new Intent(NewsMainActivity.this, SportsNewsActivity.class));
            });
        }

        // Exam news card click
        if (examNewsCard != null) {
            examNewsCard.setOnClickListener(v -> {
                startActivity(new Intent(NewsMainActivity.this, ExamNewsActivity.class));
            });
        }

        // Academic news card click
        if (academicNewsCard != null) {
            academicNewsCard.setOnClickListener(v -> {
                startActivity(new Intent(NewsMainActivity.this, FacultyNewsActivity.class));
            });
        }

        // Logout button logic: clear saved login info and go to Login screen, clear backstack
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                SharedPreferences preferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
                preferences.edit().clear().apply(); // Clear saved login data

                Intent intent = new Intent(NewsMainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
                startActivity(intent);
                finish(); // Finish current activity to prevent back navigation
            });
        }

        // Bottom navigation view listener
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
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
}
