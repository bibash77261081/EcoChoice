package com.example.ecochoice.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ecochoice.MainActivity;
import com.example.ecochoice.R;
import com.example.ecochoice.adapter.ProductAdapter;
import com.example.ecochoice.model.Product;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProductScanFragment extends Fragment implements ProductAdapter.OnItemClickListener{

    private Button scanButton;
    private ImageView productImageView;
    private TextView productNameTextView;
    private TextView productDescriptionTextView;
    private TextView environmentalEffectTextView;
    private TextView ecoFriendlyTipsTextView;
    private TextView alternativeProductsTextView;
    private LinearLayout resultView;
    private Button addButton;
    private DatabaseReference productsRef;
    private String storedImageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_scan, container, false);

        productsRef = FirebaseDatabase.getInstance().getReference().child("products");

        scanButton = view.findViewById(R.id.scanButton);
        productImageView = view.findViewById(R.id.productImageView);
        productNameTextView = view.findViewById(R.id.productNameTextView);
        productDescriptionTextView = view.findViewById(R.id.productDescriptionTextView);
        environmentalEffectTextView = view.findViewById(R.id.environmentalEffectTextView);
        ecoFriendlyTipsTextView = view.findViewById(R.id.ecoFriendlyTipsTextView);
        alternativeProductsTextView = view.findViewById(R.id.alternativeProductsTextView);
        resultView = view.findViewById(R.id.resultLayout);
        addButton = view.findViewById(R.id.addButton);
        storedImageUrl = "N/A";

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initiate barcode scanning
                startBarcodeScanner();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the product data to Firebase database
                saveProductToDatabase();
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resultView.setVisibility(View.VISIBLE);
                        }
                    });

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
            String imageUrl = productData.getString("imageUrl");
            String environmentalEffects = "environmentalEffects";
            String ecoFriendlyTips = "ecoFriendlyTips";
            String alternativeProducts = "alternativeProducts";

            if(imageUrl.equals(null) || imageUrl.equals("")){
                imageUrl = "Image Not Found";
            }else {
                storedImageUrl = imageUrl;
            }

            if(name.equals(null) || name.equals("")){
                name = "Name not found";
            }

            if(description.equals(null) || description.equals("")){
                description = "description not found";
            }

            // Create a new Product object with the extracted information
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setImageUrl(imageUrl);
            product.setEnvironmentalEffects(environmentalEffects);
            product.setEcoFriendlyTips(ecoFriendlyTips);
            product.setAlternativeProducts(alternativeProducts);

            return product;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }




    private void displayProductInformation(Product product) {
        if (product != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Display the retrieved product information in UI
                    Glide.with(getContext())
                            .load(storedImageUrl)
                            .placeholder(R.drawable.product_image_placeholder) // Placeholder image while loading
                            .error(R.drawable.error_image) // Error image if unable to load
                            .into(productImageView);

                    // Display the retrieved product information in UI
                    productNameTextView.setText(product.getName());
                    productDescriptionTextView.setText(product.getDescription());
                    environmentalEffectTextView.setText(product.getEnvironmentalEffects());
                    ecoFriendlyTipsTextView.setText(product.getEcoFriendlyTips());
                    alternativeProductsTextView.setText(product.getAlternativeProducts());
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

    private void saveProductToDatabase() {
        String productName = productNameTextView.getText().toString().trim();
        String productDescription = productDescriptionTextView.getText().toString().trim();
        String imageUrl = storedImageUrl.trim();
        String environmentalEffects = environmentalEffectTextView.getText().toString().trim();
        String ecoFriendlyTips = ecoFriendlyTipsTextView.getText().toString().trim();
        String alternativeProducts = alternativeProductsTextView.getText().toString().trim();

        String productId = productsRef.push().getKey();

        // Create a Product object with the scanned data
        Product product = new Product(productName, productDescription, imageUrl, environmentalEffects, ecoFriendlyTips, alternativeProducts);

        // Save the product to the Firebase database
        productsRef.child(productId).setValue(product).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Product added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to add product", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(Product product) {
        // Navigate to the ProductInformationFragment and pass the selected product
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.navigateToProductInformation(product);
    }
}
