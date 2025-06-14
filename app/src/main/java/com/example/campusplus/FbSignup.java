package com.example.campusplus;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FbSignup extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword, etConfirmPassword, editStatement;
    private CheckBox cbSaveData;
    private Button btnSignUp;
    private TextView loginText;
    private ImageView backButton;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fb_signup_main);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // UI references
        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        editStatement = findViewById(R.id.edit_statement);
        cbSaveData = findViewById(R.id.cb_save_data);
        btnSignUp = findViewById(R.id.btnSignUp);
        loginText = findViewById(R.id.logintext);
        backButton = findViewById(R.id.backButton);
        progressBar = findViewById(R.id.progressBar); // You must add this in your XML layout

        progressBar.setVisibility(View.GONE);

        btnSignUp.setOnClickListener(v -> registerUser());

        loginText.setOnClickListener(v -> {
            startActivity(new Intent(FbSignup.this, LoginActivity.class));
            finish();
        });

        backButton.setOnClickListener(v -> finish());
    }

    private void registerUser() {
        String name = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String statement = editStatement.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(name)) {
            etUsername.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email is required");
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return;
        }
        if (TextUtils.isEmpty(statement)) {
            editStatement.setError("Please enter your statement");
            return;
        }

        // Show progress
        progressBar.setVisibility(View.VISIBLE);
        btnSignUp.setEnabled(false);

        // Create Firebase user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    btnSignUp.setEnabled(true);

                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Update display name
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();
                            user.updateProfile(profileUpdates);

                            // Save to Firestore
                            String userId = user.getUid();
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("name", name);
                            userMap.put("email", email);
                            userMap.put("statement", statement);

                            firestore.collection("Users").document(userId)
                                    .set(userMap)
                                    .addOnSuccessListener(aVoid -> {
                                        // Send email verification
                                        user.sendEmailVerification()
                                                .addOnCompleteListener(verifyTask -> {
                                                    if (verifyTask.isSuccessful()) {
                                                        Toast.makeText(this, "Signup successful! Please verify your email.", Toast.LENGTH_LONG).show();
                                                        clearFields();
                                                        startActivity(new Intent(FbSignup.this, NewsMainActivity.class));
                                                        finish();
                                                    } else {
                                                        Toast.makeText(this, "Signup done, but verification email failed to send.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Error saving user data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    });
                        }
                    } else {
                        Toast.makeText(this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void clearFields() {
        etUsername.setText("");
        etEmail.setText("");
        etPassword.setText("");
        etConfirmPassword.setText("");
        editStatement.setText("");
    }
}
