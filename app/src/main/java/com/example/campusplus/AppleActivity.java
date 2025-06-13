package com.example.campusplus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AppleActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button loginButton;
    private ImageView backButton;
    private TextView signupText;
    private CheckBox checkboxSaveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apple_main);

        // Initialize views
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.login_button);
        backButton = findViewById(R.id.backButton);
        signupText = findViewById(R.id.signup_text);
        checkboxSaveData = findViewById(R.id.checkboxSaveData);

        // Back Button - Go to previous activity
        backButton.setOnClickListener(v -> finish());

        // Login Button - Move to NewsMainActivity
        loginButton.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Simple input validation (optional)
            if (username.isEmpty()) {
                etUsername.setError("Username required");
                return;
            }
            if (password.isEmpty()) {
                etPassword.setError("Password required");
                return;
            }

            // You can add authentication logic here if needed

            // Move to NewsMainActivity
            Intent intent = new Intent(AppleActivity.this, NewsMainActivity.class);
            startActivity(intent);
        });

        // Sign Up Text - Move to SignupActivity
        signupText.setOnClickListener(v -> {
            Intent intent = new Intent(AppleActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
}
