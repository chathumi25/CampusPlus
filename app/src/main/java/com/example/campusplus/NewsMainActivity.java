package com.example.campusplus;



import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NewsMainActivity extends AppCompatActivity {

    private EditText searchBar;
    private ImageView profileIcon;
    private TextView sportsNewsCard, examNewsCard, academicNewsCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_main);

        // Profile Icon Click
        profileIcon = findViewById(R.id.profile_icon);
        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(NewsMainActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        // Search Bar
        searchBar = findViewById(R.id.searchBar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Implement search logic (filter views or load search result activity)
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        // Section Cards (use TextViews to overlay touch zones)
        sportsNewsCard = findViewById(R.id.sports_card);
        sportsNewsCard.setOnClickListener(v -> {
            startActivity(new Intent(this, SportsNewsActivity.class));
        });

        examNewsCard = findViewById(R.id.exam_card);
        examNewsCard.setOnClickListener(v -> {
            startActivity(new Intent(this, ExamNewsActivity.class));
        });

        academicNewsCard = findViewById(R.id.academic_card);
        academicNewsCard.setOnClickListener(v -> {
            startActivity(new Intent(this, FacultyNewsActivity.class));
        });


    }
}
