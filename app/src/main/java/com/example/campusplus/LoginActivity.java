package com.example.campusplus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private CheckBox checkboxSaveData;
    private Button btnLogin;
    private TextView signUp;

    private ImageView fbIcon, twitterIcon, googleIcon, appleIcon;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private static final String TAG = "LoginDebug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        checkboxSaveData = findViewById(R.id.checkboxSaveData);
        btnLogin = findViewById(R.id.btnLogin);
        signUp = findViewById(R.id.signUp);

        fbIcon = findViewById(R.id.facebook);
        twitterIcon = findViewById(R.id.twitter);
        googleIcon = findViewById(R.id.google);
        appleIcon = findViewById(R.id.apple);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loadSavedData();

        btnLogin.setOnClickListener(v -> {
            String input = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (input.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email/username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // First check if input is an email
            db.collection("Users")
                    .whereEqualTo("email", input)
                    .get()
                    .addOnSuccessListener(emailSnapshot -> {
                        if (!emailSnapshot.isEmpty()) {
                            // Input is email
                            signInWithEmail(input, password);
                        } else {
                            // Try using as a username
                            db.collection("Users")
                                    .whereEqualTo("name", input)
                                    .get()
                                    .addOnSuccessListener(nameSnapshot -> {
                                        if (!nameSnapshot.isEmpty()) {
                                            String foundEmail = nameSnapshot.getDocuments().get(0).getString("email");
                                            if (foundEmail != null) {
                                                signInWithEmail(foundEmail, password);
                                            } else {
                                                Toast.makeText(this, "Email not found for username", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Username lookup failed: " + e.getMessage());
                                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Email lookup failed: " + e.getMessage());
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        fbIcon.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, FbActivity.class));
        });

        twitterIcon.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, TwitterActivity.class));
        });

        googleIcon.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, GoogleActivity.class));
        });

        appleIcon.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, AppleActivity.class));
        });
    }

    private void signInWithEmail(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

                    if (checkboxSaveData.isChecked()) {
                        saveUserData(email, password);
                    } else {
                        clearSavedUserData();
                    }

                    startActivity(new Intent(LoginActivity.this, NewsMainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Login failed: " + e.getMessage());
                    Toast.makeText(this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveUserData(String username, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Username", username);
        editor.putString("Password", password);
        editor.apply();
    }

    private void clearSavedUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private void loadSavedData() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("Username", "");
        String savedPassword = sharedPreferences.getString("Password", "");
        etUsername.setText(savedUsername);
        etPassword.setText(savedPassword);
        checkboxSaveData.setChecked(!savedUsername.isEmpty());
    }
}
