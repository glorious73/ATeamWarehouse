package com.example.glorious.warehousemanagement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordActivity extends AppCompatActivity {

    // Variables
    String email;
    // UI
    EditText edtEmail;
    Button btnReset;
    Button btnBack;

    // Firebase
    private FirebaseAuth mAuth;

    public ResetPasswordActivity() {
        // Initialize Firebase App & Auth
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        /* Code for the logo */
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        // Initialize stuff
        edtEmail = (EditText) findViewById(R.id.edtEmailReset);
        btnReset = (Button) findViewById(R.id.btnResetPassword);
        btnBack  = (Button) findViewById(R.id.btnBack);
        // Listeners
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailInFirebase();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Take me to SignInActivity
                Intent intent = new Intent(ResetPasswordActivity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    protected void checkEmailInFirebase() {
        // Email input
        email = edtEmail.getText().toString().isEmpty() ? "" : edtEmail.getText().toString();
        // Check valid email address
        if(email.equals("") || !(email.matches("\\b[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b"))) {
            Toast.makeText(ResetPasswordActivity.this, "Please enter your registered email to reset password.", Toast.LENGTH_LONG).show();
            return;
        }
        // Send a password reset email
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "Email sent.");
                    Toast.makeText(ResetPasswordActivity.this, "Password reset email was sent to " + email + " successfully.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Something went wrong. :(", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
