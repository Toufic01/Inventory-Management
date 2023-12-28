package com.example.inventorymangement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    EditText name, email,password;
    Button submit;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private String userType; // for storing user type

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserTypeDialog();
            }
        });
    }

    private void showUserTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select User Type")
                .setItems(R.array.user_types, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            userType = "owner";
                        } else {
                            userType = "manager";
                        }
                        // Once the user type is selected, proceed with user registration
                        registerUser();
                    }
                });
        AlertDialog dialog = builder.create();
        builder.show();
    }

    private void registerUser() {
        final String username = name.getText().toString().trim();
        final String userEmail = email.getText().toString().trim();
        final String userPassword = password.getText().toString().trim(); // Use the actual password entered by the user

        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Email or password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userPassword.length() < 6) {
            Toast.makeText(RegisterActivity.this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User registration successful, now add user data to Firestore
                            addUserToFirestore(username, userEmail, userType, userPassword);
                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                            Log.e("RegistrationActivity", "Registration failed", task.getException());
                        }
                    }
                });
    }


    private void addUserToFirestore(String username, String email, String userType, String password) {
        User user = new User(username, email, password, userType); // Include the password in User class

        firestore.collection("users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            // User data added to Firestore
                            Toast.makeText(RegisterActivity.this, "User data added to Firestore", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle failure
                            Toast.makeText(RegisterActivity.this, "Failed to add user data to Firestore", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
