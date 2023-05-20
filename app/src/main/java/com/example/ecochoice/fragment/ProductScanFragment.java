package com.example.ecochoice.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.ecochoice.R;
import com.example.ecochoice.Model.Product;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProductScanFragment extends Fragment {

    private Button scanButton;
    private TextView productNameTextView;
    private TextView productDescriptionTextView;
    private LinearLayout resultView;

    private Button addButton;
    private DatabaseReference productsRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_scan, container, false);

        productsRef = FirebaseDatabase.getInstance().getReference().child("products");

        scanButton = view.findViewById(R.id.scanButton);
        productNameTextView = view.findViewById(R.id.productNameTextView);
        productDescriptionTextView = view.findViewById(R.id.productDescriptionTextView);
        resultView = view.findViewById(R.id.resultLayout);
        addButton = view.findViewById(R.id.addButton);
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
//        integrator.setOrientationLocked(false); // Unlock orientation
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
        OkHttpClient client = new OkHttpClient();

        // Construct the URL for the API request with the scanned barcode
        String apiUrl = "https://product-lookup-by-upc-or-ean.p.rapidapi.com/code/" + barcode;

        Request request = new Request.Builder()
                .url(apiUrl)
                .get()
                .addHeader("X-RapidAPI-Key", "aef0cb7666mshf3f69b0268c2a95p111065jsne6c2e5706e9f")
                .addHeader("X-RapidAPI-Host", "product-lookup-by-upc-or-ean.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle the API request failure
                handleApiRequestFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    resultView.setVisibility(View.VISIBLE);

                    String responseData = response.body().string();
                    // Parse the response data and retrieve the product information
                    Product product = parseProductInformation(responseData);
                    // Display the retrieved product information in UI
                    displayProductInformation(product);
                } else {
                    // Handle the API request error
                    handleApiRequestError(response);
                }
            }
        });
    }

    private Product parseProductInformation(String responseData) {
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            JSONObject productData = jsonObject.getJSONObject("product");

            // Extract the name and description from the JSON object
            String name = productData.getString("name");
            String description = productData.getString("description");

            // Create a new Product object with the extracted information
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);

            return product;
        } catch (JSONException e) {
            e.printStackTrace();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), "Product Not Found.", Toast.LENGTH_LONG).show();
                }
            });
            return null;
        }
    }




    private void displayProductInformation(Product product) {
        if (product != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Display the retrieved product information in UI
                    productNameTextView.setText(product.getName());
                    productDescriptionTextView.setText(product.getDescription());
                }
            });
        } else {
            // Handle the case when the product is null
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), "Product information is unavailable.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void handleApiRequestFailure(IOException e) {
        // Handle the API request failure
        e.printStackTrace();
        // Example: Show a Toast message
        Toast.makeText(getContext(), "API request failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void handleApiRequestError(Response response) {
        // Handle the API request error
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int statusCode = response.code();
                String errorMessage = response.message();
                // Show a Toast message on the main UI thread
                Toast.makeText(getActivity(), "Product Not Found: " + statusCode + " - " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

}