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

public class SignupActivity extends AppCompatActivity {

    EditText etUsername, etPassword, etConfirmPassword, etEmail;
    CheckBox cbSaveData;
    Button btnSignUp;
    TextView tvAlreadyAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_main); // Use your signup XML file name

        // Initialize views
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        etEmail = findViewById(R.id.et_email);
        cbSaveData = findViewById(R.id.cb_save_data);
        btnSignUp = findViewById(R.id.btnLogin);
        tvAlreadyAccount = findViewById(R.id.tv_already_account); // ✅ FIXED

        // SignUp Button Click
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();
                String email = etEmail.getText().toString().trim();

                if (validateInputs(username, password, confirmPassword, email)) {
                    if (cbSaveData.isChecked()) {
                        saveUserData(username, email, password);
                    }
                    startActivity(new Intent(SignupActivity.this, NewsMainActivity.class));
                    finish();
                }
            }
        });

        // Already Have Account Text Click
        tvAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private boolean validateInputs(String username, String password, String confirmPassword, String email) {
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveUserData(String username, String email, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Username", username);
        editor.putString("Email", email);
        editor.putString("Password", password);
        editor.apply();
    }
}
