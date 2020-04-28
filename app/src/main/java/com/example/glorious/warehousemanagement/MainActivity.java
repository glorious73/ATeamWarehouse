package com.example.glorious.warehousemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // BottomNavbar
    private BottomNavigationView navigation;
    private BottomNavbar navbar;
    private final int HOME_INDEX = 0;
    // UI
    ListView listView;
    // Firebase
    private FirebaseAuth mAuth;
    /*
    * one user for the app accessed in the same manner across all activities
    * For example: in class AddActivity we'd say
    *   MainActivity.user.getUserId();
    * In SettingsActivity we'd say:
    *   MainActivity.user.getDisplayName();
    * etc...
    * */
    static FirebaseUser user;
    private DatabaseReference databaseReference;

    // Products in FirebaseDB
    List<Product> productList;
    // This variable exists to make the starting point of the app the "Sign in" screen.
    static int app_started = 0;

    public MainActivity() {
        // Initialize Firebase App & Auth
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
    }
    // Check if user is already signed in
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly. (Google's Documentation)
        MainActivity.user = mAuth.getCurrentUser();
        // ---------- READING FROM FIREBASE ---------
        // Listening for item change
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // clear the list then overwrite it
                productList.clear();
                // Iterate through products and show them
                for(DataSnapshot productSnapshot: dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    productList.add(product);
                }
                // Now add them to the list on the UI
                customAdapter adapter = new customAdapter(MainActivity.this, productList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Same as added
                // clear the list then overwrite it
                productList.clear();
                // Iterate through products and show them
                for(DataSnapshot productSnapshot: dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    productList.add(product);
                }
                // Now add them to the list on the UI
                customAdapter adapter = new customAdapter(MainActivity.this, productList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // clear the list then overwrite it
                productList.clear();
                // Iterate through products and show them
                for(DataSnapshot productSnapshot: dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    productList.add(product);
                }
                // Now add them to the list on the UI
                customAdapter adapter = new customAdapter(MainActivity.this, productList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Start the app from SignInActivity (app_started condition runs only once)
        if (MainActivity.app_started == 0) {
            MainActivity.app_started++;
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
            finish();
        }
        /* Code for the logo */
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        // Navigation code
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navbar = new BottomNavbar();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navbar.assignIcon(navigation, HOME_INDEX);
        // UI
        listView = findViewById(R.id.lstProducts);
        // Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference(); // start from root
        // Variables
        productList = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Assign respective icon
        navbar.assignIcon(navigation, HOME_INDEX);
        // OverridePendingTransition (remove animation)
        overridePendingTransition(0, 0);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            navbar.switchToActivity(MainActivity.this, item.getItemId());
            return true;
        }
    };

}
