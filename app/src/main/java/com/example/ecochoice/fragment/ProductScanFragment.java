package com.example.ecochoice.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.ecochoice.R;
import com.example.ecochoice.database.Product;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductScanFragment extends Fragment {

    private Button scanButton;
    private DatabaseReference productsRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_scan, container, false);

        productsRef = FirebaseDatabase.getInstance().getReference().child("products");

        scanButton = view.findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initiate barcode scanning
                startBarcodeScanner();
            }
        });

        return view;
    }

    public void initiateBarcodeScan() {
        startBarcodeScanner();
    }

    private void startBarcodeScanner() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setCameraId(0); // Use rear camera
        integrator.setBeepEnabled(false);
        integrator.setOrientationLocked(false); // Unlock orientation
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            String scannedBarcode = result.getContents();
            // Handle the scanned barcode
            fetchProductInformation(scannedBarcode);
        }
    }

    private void fetchProductInformation(String barcode) {
        productsRef.child(barcode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    // Display the retrieved product information in your UI
                    displayProductInformation(product);
                } else {
                    // Product not found in the database
                    handleProductNotFound();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
                handleDatabaseError(databaseError);
            }
        });
    }

    private void displayProductInformation(Product product) {
        // Display the retrieved product information in your UI
        // For example, update TextViews or ImageView with the product data
    }

    private void handleProductNotFound() {
        // Handle the case when the product is not found in the database
        // For example, show an error message or perform a fallback action
    }

    private void handleDatabaseError(DatabaseError databaseError) {
        // Handle the database error
        // For example, show an error message or perform error-specific actions
    }
}
