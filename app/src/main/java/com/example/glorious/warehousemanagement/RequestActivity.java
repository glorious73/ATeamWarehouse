package com.example.glorious.warehousemanagement;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RequestActivity extends AppCompatActivity {

    // Bottom Navbar
    private BottomNavigationView navigation;
    private BottomNavbar navbar;
    private final int REQUEST_INDEX = 2;
    // UI
    Spinner spnNames;
    EditText edtQuantity;
    EditText edtCName;
    EditText edtPhone;
    Button btnRequest;
    // Variables
    String pName;
    int quantity;
    String cName;
    String phone;
    // Firebase
    private DatabaseReference databaseReference;
    // Products in FirebaseDB
    List<Product> productList;

    @Override
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(RequestActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(RequestActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(RequestActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
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
        setContentView(R.layout.activity_request);
        /* Code for the logo */
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        // Navbar
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navbar = new BottomNavbar();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navbar.assignIcon(navigation, REQUEST_INDEX);
        // Initialize stuff
        spnNames    = (Spinner) findViewById(R.id.spnNames);
        edtQuantity = (EditText) findViewById(R.id.edtQuantity);
        edtCName    = (EditText) findViewById(R.id.edtCName);
        edtPhone    = (EditText) findViewById(R.id.edtPhone);
        btnRequest  = (Button) findViewById(R.id.btnRequest);
        // Listeners
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestProduct();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Assign respective icon
        navbar.assignIcon(navigation, REQUEST_INDEX);
        // OverridePendingTransition (remove animation)
        overridePendingTransition(0, 0);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            navbar.switchToActivity(RequestActivity.this, item.getItemId());
            return true;
        }
    };

    private void requestProduct() {
        // check valid entries
        try {
            pName     = spnNames.getSelectedItem().toString();
            quantity  = edtQuantity.getText().toString().isEmpty() ? -1 : Integer.parseInt(edtQuantity.getText().toString());
            cName     = edtCName.getText().toString().isEmpty() ? "" : edtCName.getText().toString();
            phone     = edtPhone.getText().toString().isEmpty() ? "" : edtPhone.getText().toString();
            if(quantity == -1 || cName.equals("") || phone.equals("") || !phone.matches("05[0-9]{8}")) {
                Toast.makeText(RequestActivity.this, "Error. Please fill the fields properly and try again.", Toast.LENGTH_SHORT).show();
                return;
            }
            // Now check the product against the quantity and request it
            Product pRequested = null;
            for(Product p: productList) {
                if(pName.equals(p.getName())) {
                    pRequested = p;
                }
            }
            if(pRequested != null) {
                if(quantity < pRequested.getQuantity()) {
                    String result = sendProductRequest(pRequested);
                    Toast.makeText(RequestActivity.this, result, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RequestActivity.this, "Not enough in stock", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception ex) {
            Toast.makeText(RequestActivity.this, "Error. Please try again.", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }

    private String sendProductRequest(Product product) {
        // send an email to admins
        String[] TO = {"majjoud.97@gmail.com"};
        String[] CC = {"mfbalghunaim@gmail.com", "amaldayel@alfaisal.edu"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Product request");
        emailIntent.putExtra(Intent.EXTRA_TEXT, MainActivity.user.getEmail().substring(0, MainActivity.user.getEmail().indexOf("@")) + " would like to request " + quantity + " items of " + product.getName() + "\n\n More details:\n - Customer name: " + cName + "\n - Customer's phone number: " + phone +  "\n\nRegards.");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...", "");
            return "Sending email to majjoud.97@gmail.com";
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(RequestActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
        return "Operation failed";
    }
}
