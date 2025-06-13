package com.example.campusplus;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    // Declare DatabaseReference variable
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Database reference pointing to "users" node
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Save sample user data to Firebase
        saveUserData();
    }

    private void saveUserData() {
        // Generate a unique id for the new user
        String userId = databaseReference.push().getKey();

        // Create a user object
        User user = new User("Chathumi", "chathumi@example.com");

        // Save the user object under the generated id
        if (userId != null) {
            databaseReference.child(userId).setValue(user)
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(MainActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show()
                    )
                    .addOnFailureListener(e ->
                            Toast.makeText(MainActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show()
                    );
        } else {
            Toast.makeText(this, "Failed to generate user ID", Toast.LENGTH_SHORT).show();
        }
    }

    // User class to hold user info
    public static class User {
        public String name;
        public String email;

        // Default constructor (required)
        public User() {}

        public User(String name, String email) {
            this.name = name;
            this.email = email;
        }
    }
}
