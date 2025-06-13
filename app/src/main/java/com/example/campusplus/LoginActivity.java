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

    DatabaseReference databaseReference; // Firebase DB reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        // Initialize views
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        checkboxSaveData = findViewById(R.id.checkboxSaveData);
        btnLogin = findViewById(R.id.btnLogin);
        signUp = findViewById(R.id.signUp);

        // Firebase reference to "users" node
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        loadSavedData();

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

            // ✅ Save to Firebase
            String userId = databaseReference.push().getKey(); // unique key
            User user = new User(username, password); // see User class below
            if (userId != null) {
                databaseReference.child(userId).setValue(user);
            }

            // Navigate to NewsActivity
            Intent intent = new Intent(LoginActivity.this, NewsMainActivity.class);
            startActivity(intent);
            finish();
        });

        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        // Social media image click handling
        findViewById(R.id.facebook).setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, FbActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.twitter).setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, TwitterActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.google).setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, GoogleActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.apple).setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, AppleActivity.class);
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

    // 👇 Helper class for user model
    public static class User {
        public String username;
        public String password;

        public User() {} // Needed for Firebase

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
