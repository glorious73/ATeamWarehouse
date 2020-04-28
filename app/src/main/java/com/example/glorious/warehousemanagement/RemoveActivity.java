package com.example.glorious.warehousemanagement;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RemoveActivity extends AppCompatActivity {

    // Are you sure you want to remove product from warehouse?
    int doubleCheck = 0;
    // Navbar
    private BottomNavigationView navigation;
    private BottomNavbar navbar;
    private final int REMOVE_INDEX = 3;
    // UI
    Spinner spnNames;
    Button btnRemove;
    TextView txtCheckRemove;
    // Firebase
    private DatabaseReference databaseReference;

    // Products in FirebaseDB
    List<Product> productList;

    // Product name
    String pName;

    public void onStart() {
        super.onStart();
        // ---------- READING FROM FIREBASE ---------
        // Initialize stuff
        spnNames          = (Spinner) findViewById(R.id.spnNames);
        databaseReference = FirebaseDatabase.getInstance().getReference(); // start from root
        // Variables
        productList = new ArrayList<>();
        // Listening for item change
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // clear the list then overwrite it
                productList.clear();
                // We want the spinner to display product names
                List<String> spinnerArray = new ArrayList<String>();
                // Iterate through products and show them
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    productList.add(product);
                    spinnerArray.add(product.getName());
                }
                // Now add the names to the spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(RemoveActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnNames.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Same as added
                // clear the list then overwrite it
                productList.clear();
                // We want the spinner to display product names
                List<String> spinnerArray = new ArrayList<String>();
                // Iterate through products and show them
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    productList.add(product);
                    spinnerArray.add(product.getName());
                }
                // Now add the names to the spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(RemoveActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnNames.setAdapter(adapter);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // Same as added also
                // clear the list then overwrite it
                productList.clear();
                // We want the spinner to display product names
                List<String> spinnerArray = new ArrayList<String>();
                // Iterate through products and show them
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    productList.add(product);
                    spinnerArray.add(product.getName());
                }
                // Now add the names to the spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(RemoveActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnNames.setAdapter(adapter);
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
        setContentView(R.layout.activity_remove);
        /* Code for the logo */
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        // Navbar
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navbar = new BottomNavbar();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navbar.assignIcon(navigation, REMOVE_INDEX);
        // Initialize stuff
        spnNames       = (Spinner) findViewById(R.id.spnNames);
        btnRemove      = (Button) findViewById(R.id.btnRemove);
        txtCheckRemove = (TextView) findViewById(R.id.txtViewCheckRemove);
        // Listeners
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeProduct();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Assign respective icon
        navbar.assignIcon(navigation, REMOVE_INDEX);
        // OverridePendingTransition (remove animation)
        overridePendingTransition(0, 0);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            navbar.switchToActivity(RemoveActivity.this, item.getItemId());
            return true;
        }
    };

    private void removeProduct() {
        if(doubleCheck == 0) {
            txtCheckRemove.setText("Are you sure you want to remove product " + spnNames.getSelectedItem().toString() + "?\n\nPress again to remove");
            doubleCheck++;
            return;
        }
        pName = spnNames.getSelectedItem().toString();
        // remove product after matching by name
        for(Product p: productList) {
            if(p.getName().equalsIgnoreCase(pName)) {
                // Locate product to be deleted in DB
                DatabaseReference dbProducts = FirebaseDatabase.getInstance().getReference().getRoot().child("products").child(p.getProductFirebaseId());
                // remove that value from DB
                dbProducts.removeValue();
                // Tell user that stuff was deleted
                Toast.makeText(RemoveActivity.this, "Deleted " + p.getName() + " successfully", Toast.LENGTH_LONG).show();
            }
        }
        // Reset double checking
        doubleCheck = 0;
        txtCheckRemove.setText("");
    }
}