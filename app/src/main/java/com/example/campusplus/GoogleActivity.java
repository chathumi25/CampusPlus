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

public class GoogleActivity extends AppCompatActivity {

    private ImageView backButton;
    private EditText etUsername, etPassword;
    private Button loginButton;
    private TextView signupText;
    private CheckBox checkboxSaveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_main); // Your layout file

        // Bind UI components
        backButton = findViewById(R.id.backButton);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.login_button);
        signupText = findViewById(R.id.signup_text);
        checkboxSaveData = findViewById(R.id.checkboxSaveData);

        // Back button returns to previous screen
        backButton.setOnClickListener(v -> finish());

        // Login button moves to NewsActivity
        loginButton.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                // You can add authentication logic here if needed
                Intent intent = new Intent(GoogleActivity.this, NewsMainActivity.class);
                startActivity(intent);
            } else {
                if (username.isEmpty()) {
                    etUsername.setError("Username required");
                }
                if (password.isEmpty()) {
                    etPassword.setError("Password required");
                }
            }
        });

        // Sign up text moves to SignupActivity
        signupText.setOnClickListener(v -> {
            Intent intent = new Intent(GoogleActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
}
