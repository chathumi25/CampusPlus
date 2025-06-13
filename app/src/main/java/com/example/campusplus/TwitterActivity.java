package com.example.campusplus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TwitterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button loginButton;
    private TextView signupText;
    private ImageView backButton;
    private CheckBox checkboxSaveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_main);

        // Initialize views
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.login_button);
        signupText = findViewById(R.id.signup_text);
        backButton = findViewById(R.id.backButton);
        checkboxSaveData = findViewById(R.id.checkboxSaveData);

        // Back button logic
        backButton.setOnClickListener(v -> finish());

        // Login button logic
        loginButton.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(TwitterActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Optional: save credentials if checkbox checked (SharedPreferences can be added here)

                // Move to NewsMainActivity
                Intent intent = new Intent(TwitterActivity.this, NewsMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Signup link logic
        signupText.setOnClickListener(v -> {
            Intent intent = new Intent(TwitterActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
}
