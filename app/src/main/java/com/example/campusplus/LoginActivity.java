package com.example.campusplus;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    CheckBox checkboxSaveData;
    Button btnLogin;
    TextView signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main); // Your layout file

        // Initialize views
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        checkboxSaveData = findViewById(R.id.checkboxSaveData);
        btnLogin = findViewById(R.id.btnLogin);
        signUp = findViewById(R.id.signUp);

        // Load saved data (optional)
        loadSavedData();

        // Login button click
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (checkboxSaveData.isChecked()) {
                saveUserData(username, password);
            }

            // TODO: Optional - validate credentials before proceeding

            // Move to NewsActivity
            Intent intent = new Intent(LoginActivity.this, NewsActivity.class);
            startActivity(intent);
            finish();
        });

        // Sign Up text click
        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    private void saveUserData(String username, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Username", username);
        editor.putString("Password", password);
        editor.apply();
    }

    private void loadSavedData() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("Username", "");
        String savedPassword = sharedPreferences.getString("Password", "");

        etUsername.setText(savedUsername);
        etPassword.setText(savedPassword);
        checkboxSaveData.setChecked(!savedUsername.isEmpty() && !savedPassword.isEmpty());
    }
}

