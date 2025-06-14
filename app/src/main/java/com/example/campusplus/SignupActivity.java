package com.example.campusplus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    EditText etUsername, etPassword, etConfirmPassword, etEmail,etPersonalStatement;
    Button btnSignUp;
    TextView tvAlreadyAccount;

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_main);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        etEmail = findViewById(R.id.et_email);
        etPersonalStatement = findViewById(R.id.edit_statement);


        btnSignUp = findViewById(R.id.btnSignUp);
        tvAlreadyAccount = findViewById(R.id.tv_already_account);

        btnSignUp.setOnClickListener(v -> registerUser());

        tvAlreadyAccount.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String personalStatement = etPersonalStatement.getText().toString().trim();

        if (!validateInputs(username, email, password, confirmPassword)) return;

        // Create user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Get user ID (UID)
                        String userId = mAuth.getCurrentUser().getUid();

                        // Prepare user data to save in Firestore
                        Map<String, Object> user = new HashMap<>();
                        user.put("name", username);
                        user.put("email", email);
                        user.put("statement", personalStatement); // Initially empty, can update later

                        // Save user data under "Users" collection with document id = userId
                        firestore.collection("Users").document(userId)
                                .set(user)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignupActivity.this, NewsMainActivity.class));
                                    finish();
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Failed to save user data: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                );
                    } else {
                        Toast.makeText(this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean validateInputs(String username, String email, String password, String confirmPassword) {
        if (username.isEmpty()) {
            etUsername.setError("Username required");
            etUsername.requestFocus();
            return false;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email required");
            etEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password required");
            etPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }
}
