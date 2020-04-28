package com.example.glorious.warehousemanagement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    // Variables
    String email;
    String password;
    // UI
    EditText edtEmail;
    EditText edtPassword;
    Button btnLogin;
    TextView txtForgotPassword;
    // Firebase
    private FirebaseAuth mAuth;

    public SignInActivity() {
        // Initialize Firebase App & Auth
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        // Initializing stuff
        txtForgotPassword     = (TextView) findViewById(R.id.txtForgotPassword);
        edtEmail              = (EditText) findViewById(R.id.edtEmail);
        edtPassword           = (EditText) findViewById(R.id.edtPass);
        btnLogin              = (Button) findViewById(R.id.btnLogin);
        // Listeners
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFirebase();
            }
        });
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Email_Reset_Password activity
                Intent intent = new Intent(SignInActivity.this, ResetPasswordActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    protected void loginFirebase() {
        // Get user input
        email    = edtEmail.getText().toString().isEmpty() ? "" : edtEmail.getText().toString();
        password = edtPassword.getText().toString().isEmpty() ? "" : edtPassword.getText().toString();
        // Check empty strings or invalid email addresses and inform user
        if(email.equals("") || password.equals("") || !(email.matches("\\b[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b"))) {
            Toast.makeText(SignInActivity.this, "Please fill the fields properly to sign in.", Toast.LENGTH_LONG).show();
            return;
        }
        // Log the user in
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success
                    Log.d("TAG", "signInWithEmail:success");
                    // Clear fields
                    edtEmail.setText("");
                    edtPassword.setText("");
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(SignInActivity.this, "Sign in Success", Toast.LENGTH_LONG).show();
                    // Navigate to HomeActivity (MainActivity)
                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithEmail:failure", task.getException());
                    Toast.makeText(SignInActivity.this, "Login failed. Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
