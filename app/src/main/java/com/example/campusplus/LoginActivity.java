package com.example.campusplus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    CheckBox checkboxSaveData;
    Button btnLogin;
    TextView signUp;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        // Bind views
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        checkboxSaveData = findViewById(R.id.checkboxSaveData);
        btnLogin = findViewById(R.id.btnLogin);
        signUp = findViewById(R.id.signUp);

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Load saved username/password if previously saved
        loadSavedData();

        // Login Button Click
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            boolean isValid = true;

            // Field validation
            if (username.isEmpty()) {
                etUsername.setError("Username is required");
                isValid = false;
            }
            if (password.isEmpty()) {
                etPassword.setError("Password is required");
                isValid = false;
            }

            if (!isValid) return;

            // Save login credentials if checkbox is checked
            if (checkboxSaveData.isChecked()) {
                saveUserData(username, password);
            }

            // 🔒 NOTE: This is not a secure login method – for demo only
            String userId = databaseReference.push().getKey();
            if (userId != null) {
                User user = new User(username, password);
                databaseReference.child(userId).setValue(user);
            }

            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

            // Navigate to News Activity
            Intent intent = new Intent(LoginActivity.this, NewsMainActivity.class);
            startActivity(intent);
            finish();
        });

        // Sign Up Text Click
        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        // Social media login redirects
        findViewById(R.id.facebook).setOnClickListener(v -> {
            startActivity(new Intent(this, FbActivity.class));
        });

        findViewById(R.id.twitter).setOnClickListener(v -> {
            startActivity(new Intent(this, TwitterActivity.class));
        });

        findViewById(R.id.google).setOnClickListener(v -> {
            startActivity(new Intent(this, GoogleActivity.class));
        });

        findViewById(R.id.apple).setOnClickListener(v -> {
            startActivity(new Intent(this, AppleActivity.class));
        });
    }

    // Save credentials using SharedPreferences
    private void saveUserData(String username, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Username", username);
        editor.putString("Password", password);
        editor.apply();
    }

    // Load credentials if saved
    private void loadSavedData() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("Username", "");
        String savedPassword = sharedPreferences.getString("Password", "");

        etUsername.setText(savedUsername);
        etPassword.setText(savedPassword);
        checkboxSaveData.setChecked(!savedUsername.isEmpty() && !savedPassword.isEmpty());
    }

    // User model for Firebase
    public static class User {
        public String username;
        public String password;

        public User() {} // Required for Firebase

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
