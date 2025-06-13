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

public class FbActivity extends AppCompatActivity {

    private ImageView backButton;
    private EditText etUsername, etPassword;
    private Button loginButton;
    private TextView signupText;
    private CheckBox checkboxSaveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fb_main);

        // Initialize Views
        backButton = findViewById(R.id.backButton);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.login_button);
        signupText = findViewById(R.id.signup_text);
        checkboxSaveData = findViewById(R.id.checkboxSaveData);

        // Back button to previous activity
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // goes to previous screen
            }
        });

        // Login button to News screen
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (!username.isEmpty() && !password.isEmpty()) {
                    // Add validation or login logic if needed

                    Intent intent = new Intent(FbActivity.this, NewsMainActivity.class); // Replace with your News screen class name
                    startActivity(intent);
                } else {
                    etUsername.setError("Required");
                    etPassword.setError("Required");
                }
            }
        });

        // Sign up text click to Register screen
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FbActivity.this, SignupActivity.class); // Replace with your Register screen class name
                startActivity(intent);
            }
        });
    }
}

