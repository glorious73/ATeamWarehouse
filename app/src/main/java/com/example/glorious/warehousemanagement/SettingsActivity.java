package com.example.glorious.warehousemanagement;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class SettingsActivity extends AppCompatActivity {

    // Navigation
    private BottomNavigationView navigation;
    private BottomNavbar navbar;
    private final int SETTINGS_INDEX = 4;
    // UI
    TextView txtUsername;
    TextView txtRole;
    Button btnCompany;
    Button btnOut;
    // Firebase
    private FirebaseAuth mAuth;
    // Class variables

    public SettingsActivity() {
        // Initialize Firebase Auth in settings activity
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // Navigation
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navbar = new BottomNavbar();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navbar.assignIcon(navigation, SETTINGS_INDEX);
        // UI
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtRole     = (TextView) findViewById(R.id.txtRole);
        btnCompany  = (Button) findViewById(R.id.btnCompany);
        btnOut      = (Button) findViewById(R.id.btnOut);
        // Visuals
        txtUsername.setText(MainActivity.user.getEmail().substring(0, MainActivity.user.getEmail().indexOf("@")));
        txtRole.setText("Software Developer");
        // Listeners
        btnCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.alfaisal.edu/")));
            }
        });
        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutFirebase();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Assign respective icon
        navbar.assignIcon(navigation, SETTINGS_INDEX);
        // OverridePendingTransition (remove animation)
        overridePendingTransition(0, 0);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            navbar.switchToActivity(SettingsActivity.this, item.getItemId());
            return true;
        }
    };

    protected void signOutFirebase() {
        mAuth.signOut(); // straightforward
        Toast.makeText(SettingsActivity.this, "Signed out successfully", Toast.LENGTH_LONG).show();
        Intent outIntent = new Intent(SettingsActivity.this, SignInActivity.class);
        outIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(outIntent);
    }
}
