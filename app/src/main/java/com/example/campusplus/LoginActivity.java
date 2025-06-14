package com.example.campusplus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log; // Added for logging Firestore operations
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

// Firestore imports
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions; // Crucial for merging data

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap; // For Firestore data structure
import java.util.Map;     // For Firestore data structure

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity"; // For logging Firestore operations

    EditText etUsername, etPassword;
    CheckBox checkboxSaveData;
    Button btnLogin;
    TextView signUp;

    DatabaseReference databaseReference;
    private FirebaseFirestore db; // Declare Firestore instance

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

        // Initialize Firebase Realtime Database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users"); // This line seems redundant if you're using databaseReference below
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Initialize Firebase Firestore instance
        db = FirebaseFirestore.getInstance();

        // Load saved username/password if previously saved (from SharedPreferences)
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

            // Save login credentials using SharedPreferences if checkbox is checked
            if (checkboxSaveData.isChecked()) {
                saveUserData(username, password);
            }

            // --- Existing Firebase Realtime Database save (from your original code) ---
            // 🔒 NOTE: This is not a secure login method – for demo only
            // This part creates a new user entry in Realtime Database with a unique ID
            String userId = databaseReference.push().getKey();
            if (userId != null) {
                User user = new User(username, password);
                databaseReference.child(userId).setValue(user)
                        .addOnSuccessListener(aVoid -> Log.d(TAG, "User data saved to Realtime Database successfully."))
                        .addOnFailureListener(e -> Log.e(TAG, "Error saving user data to Realtime Database: " + e.getMessage()));
            }
            // --- End of existing Realtime Database save ---

            // --- NEW: Save login credentials to Firestore (as per your requirement) ---
            saveLoginDataToFirestore(username, password);
            // --- END NEW ---

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

    /**
     * Saves the username and password to Firebase Firestore.
     * It targets the 'userlogin' collection and 'userlogin' document,
     * updating 'name/email' and 'password' fields.
     */
    private void saveLoginDataToFirestore(String usernameEmail, String password) {
        // Create a map to hold the data for Firestore
        Map<String, Object> userData = new HashMap<>();
        userData.put("name/email", usernameEmail); // Key matches your Firestore field
        userData.put("password", password);         // Key matches your Firestore field

        // Reference to the specific Firestore document
        // Collection: userlogin, Document: userlogin
        DocumentReference userDocRef = db.collection("userlogin").document("userlogin");

        // Use .set() with SetOptions.merge() to update existing fields or create the document if it doesn't exist.
        // This ensures other potential fields in the 'userlogin' document are not overwritten.
        userDocRef.set(userData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Login data successfully saved/updated in Firestore.");
                    // You might want a small toast here if it's crucial for the user to know it's saved.
                    // Toast.makeText(LoginActivity.this, "Login data saved to Firestore!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error saving login data to Firestore: ", e);
                    Toast.makeText(LoginActivity.this, "Failed to save login data to Firestore.", Toast.LENGTH_SHORT).show();
                });
    }

    // Save credentials using SharedPreferences
    private void saveUserData(String username, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Username", username);
        editor.putString("Password", password);
        editor.apply();
        Log.d(TAG, "Login data saved to SharedPreferences.");
    }

    // Load credentials if saved
    private void loadSavedData() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("Username", "");
        String savedPassword = sharedPreferences.getString("Password", "");

        etUsername.setText(savedUsername);
        etPassword.setText(savedPassword);
        checkboxSaveData.setChecked(!savedUsername.isEmpty() && !savedPassword.isEmpty());
        if (!savedUsername.isEmpty()) {
            Log.d(TAG, "Saved data loaded from SharedPreferences.");
        }
    }

    // User model for Firebase Realtime Database
    public static class User {
        public String username;
        public String password;

        public User() {} // Required for Firebase Realtime Database

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}