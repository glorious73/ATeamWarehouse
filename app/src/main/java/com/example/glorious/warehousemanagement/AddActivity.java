package com.example.glorious.warehousemanagement;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AddActivity extends AppCompatActivity {

    // Navbar
    private BottomNavigationView navigation;
    private BottomNavbar navbar;
    private final int ADD_INDEX = 1;
    // UI
    EditText edtName;
    EditText edtManufacturer;
    EditText edtQuantity;
    EditText edtPrice;
    EditText edtDescription;
    Button   btnChooseImage;
    Button   btnAddProduct;
    TextView txtImageName;
    Bitmap bitmap;
    // Firebase
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    // Class Variables
    private Product product;
    private Uri filePath; // the file path of the chosen image
    private final int PICK_IMAGE_REQUEST = 71; // To choose image from file explorer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        /* Code for the logo */
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        // BottomNavbar
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navbar = new BottomNavbar();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navbar.assignIcon(navigation, ADD_INDEX);
        // Firebase
        firebaseStorage   = FirebaseStorage.getInstance();
        storageReference  = firebaseStorage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        // Initialize stuff
        edtName         = (EditText) findViewById(R.id.edtName);
        edtManufacturer = (EditText) findViewById(R.id.edtManufacturer);
        edtDescription  = (EditText) findViewById(R.id.edtDescription);
        edtQuantity     = (EditText) findViewById(R.id.edtQuantity);
        edtPrice        = (EditText) findViewById(R.id.edtPrice);
        btnChooseImage  = (Button)   findViewById(R.id.btnChooseImage);
        btnAddProduct   = (Button)   findViewById(R.id.btnAddProduct);
        txtImageName    = (TextView) findViewById(R.id.txtImageName);
        // Set the default image for the product just in case the user chooses none.
        // CREDIT FOR THE NEXT LINE OF CODE: STACKOVERFLOW (https://stackoverflow.com/questions/6602417/get-the-uri-of-an-image-stored-in-drawable)
        filePath = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + AddActivity.this.getResources().getResourcePackageName(R.drawable.default_image)
                + '/' + AddActivity.this.getResources().getResourceTypeName(R.drawable.default_image)
                + '/' + AddActivity.this.getResources().getResourceEntryName(R.drawable.default_image));
        // Listeners
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductToFirebase();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Assign respective icon
        navbar.assignIcon(navigation, ADD_INDEX);
        // OverridePendingTransition (remove animation)
        overridePendingTransition(0, 0);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            navbar.switchToActivity(AddActivity.this, item.getItemId());
            return true;
        }
    };

    // ---------- Choose Image from InternalStorage -----------
    private void chooseImage() {
        // Intent to open File Explorer in order to choose the image from local storage
        Intent imageIntent = new Intent();
        imageIntent.setType("image/*");
        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(imageIntent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                // Retrieve the image
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // Set the image name on the UI
                txtImageName.setText(filePath.getPath());
            } catch (Exception ex) {
                Toast.makeText(AddActivity.this, "Error picking image. Please try again.", Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        } else {
            // set to the file path of the default image
            // CREDIT FOR THE NEXT LINE OF CODE: STACKOVERFLOW (https://stackoverflow.com/questions/6602417/get-the-uri-of-an-image-stored-in-drawable)
            filePath = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + AddActivity.this.getResources().getResourcePackageName(R.drawable.default_image)
                    + '/' + AddActivity.this.getResources().getResourceTypeName(R.drawable.default_image)
                    + '/' + AddActivity.this.getResources().getResourceEntryName(R.drawable.default_image));
        }
    }
    // --------------------------------------------------------
    // AddProduct to firebase database
    private void addProductToFirebase() {
        // Read product values
        String pName, pManu, pDesc;
        double pPrice;
        int pQuantity;
        try {
            pName     = edtName.getText().toString().isEmpty() ? "" : edtName.getText().toString();
            pManu     = edtManufacturer.getText().toString().isEmpty() ? "" : edtManufacturer.getText().toString();
            pDesc     = edtDescription.getText().toString().isEmpty() ? "" : edtDescription.getText().toString();
            pPrice    = edtPrice.getText().toString().isEmpty() ? -1 : Double.parseDouble(edtPrice.getText().toString());
            pQuantity = edtQuantity.getText().toString().isEmpty() ? -1 : Integer.parseInt(edtQuantity.getText().toString());
            if(pName.equals("") || pManu.equals("") || pDesc.equals("") || pPrice == -1 || pQuantity == -1) {
                Toast.makeText(AddActivity.this, "Please fill values properly to add a product.", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (Exception ex) {
            Toast.makeText(AddActivity.this, "Error. \"Price\" and \"Quantity\" must be numbers.", Toast.LENGTH_LONG).show();
            return;
        }
        // A random UUID will be generated for each image and that ID will be passed to the product object
        // so that each product could reference its respective image later on
        UUID imgId = UUID.randomUUID();
        // Now initialize the product (everything should be just fine at this point)
        product = new Product(pName, pManu, pDesc, pPrice, pQuantity, MainActivity.user.getUid(), ""+imgId);
        // Upload the product (POSSIBLE EXCEPTION)
        databaseReference.child("products").child(""+imgId).setValue(product); // saved under 'products' in FirebaseDB
        Toast.makeText(AddActivity.this, "Added product successfully.", Toast.LENGTH_LONG).show();
        // Upload the image (the product name is the image file name in order to map them)
        if(filePath != null && product != null) {
            Toast.makeText(AddActivity.this, "Uploading image...", Toast.LENGTH_LONG).show();
            StorageReference ref = storageReference.child(""+imgId);
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddActivity.this, "Uploaded image successfully", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddActivity.this, "Failed to upload image", Toast.LENGTH_LONG).show();
                }
            });
        }
        // And finally, clear UI fields
        clearFields();
    }

    private void clearFields() {
        edtName.setText("");
        edtManufacturer.setText("");
        edtQuantity.setText("");
        edtPrice.setText("");
        edtDescription.setText("");
    }
}
