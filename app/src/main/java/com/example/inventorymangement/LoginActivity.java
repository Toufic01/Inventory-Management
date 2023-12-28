package com.example.inventorymangement;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.annotation.Nonnull;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    Button click;
    TextView forget, account;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        click = findViewById(R.id.click);
        forget = findViewById(R.id.forget);
        account = findViewById(R.id.account);
        firebaseAuth = FirebaseAuth.getInstance();

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // work here for login
                loginUser();
            }
        });

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // work here for forget password
                showForgetPasswordDialog();
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // work here for registration
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }

    private void showForgetPasswordDialog() {
        ForgotPasswordDialogFragment dialog = new ForgotPasswordDialogFragment();
        dialog.show(getSupportFragmentManager(), "ForgotPasswordDialogFragment");
    }

    public static class ForgotPasswordDialogFragment extends DialogFragment {
        private EditText emailEditText;

        @Nonnull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            View view = requireActivity().getLayoutInflater().inflate(R.layout.dialog_forgot_password, null);
            emailEditText = view.findViewById(R.id.email);

            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setView(view)
                    .setTitle("Forgot Password")
                    .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Pass the context when calling sendPasswordResetEmail
                            sendPasswordResetEmail(requireContext());
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();
                        }
                    });

            return builder.create();
        }

        private void sendPasswordResetEmail(Context context) {
            String email = emailEditText.getText().toString().trim();

            if (!TextUtils.isEmpty(email)) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@Nonnull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Password reset email sent", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loginUser() {
        String useremail = email.getText().toString();
        String userpassword = password.getText().toString();
        if (useremail.isEmpty() || userpassword.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Fulfill the information", Toast.LENGTH_LONG).show();
            return;
        } else {
            firebaseAuth.signInWithEmailAndPassword(useremail, userpassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Check user type and display a toast
                                checkUserTypeAndToast(useremail);
                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Login failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                                Log.e("LoginActivity", "Login failed", task.getException());
                            }
                        }
                    });
        }
    }

    private void checkUserTypeAndToast(String userEmail) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String userType = document.getString("userType");
                                if ("owner".equals(userType)) {
                                    // Owner login
                                    Toast.makeText(LoginActivity.this, "Logged in as Owner", Toast.LENGTH_SHORT).show();
                                } else if ("manager".equals(userType)) {
                                    // Manager login
                                    Toast.makeText(LoginActivity.this, "Logged in as Manager", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Invalid user type
                                    Toast.makeText(LoginActivity.this, "Invalid user type", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Document does not exist
                                Toast.makeText(LoginActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Handle failure
                            Toast.makeText(LoginActivity.this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
