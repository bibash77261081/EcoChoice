package com.example.ecochoice.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ecochoice.MainActivity;
import com.example.ecochoice.R;
import com.example.ecochoice.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class ProductInformationFragment extends Fragment {
    private static final String ARG_PRODUCT = "product";

    private TextView productNameTextView;
    private TextView descriptionTextView;
    private ImageView productImageView;
    private TextView environmentalEffectTextView;
    private TextView ecoFriendlyTipsTextView;
    private TextView alternativeProductsTextView;
    private Button deleteProductButton;
    private DatabaseReference databaseReference;

    private Product product;

    public ProductInformationFragment() {
        // Required empty public constructor
    }

    public static ProductInformationFragment newInstance(Product product) {
        ProductInformationFragment fragment = new ProductInformationFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PRODUCT, product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = getArguments().getParcelable(ARG_PRODUCT);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_information, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productNameTextView = view.findViewById(R.id.productNameTextView);
        descriptionTextView = view.findViewById(R.id.productDescriptionTextView);
        productImageView = view.findViewById(R.id.productImageView);
        environmentalEffectTextView = view.findViewById(R.id.environmentalEffectTextView);
        ecoFriendlyTipsTextView = view.findViewById(R.id.ecoFriendlyTipsTextView);
        alternativeProductsTextView = view.findViewById(R.id.alternativeProductsTextView);
        deleteProductButton = view.findViewById(R.id.deleteProductButton);
        databaseReference = FirebaseDatabase.getInstance().getReference("products");

        deleteProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product != null) {
                    String productName = product.getName();
                    String productDescription = product.getDescription();

                    // Find the product in the database based on name and description
                    Query query = databaseReference.orderByChild("name").equalTo(productName)
                            .orderByChild("description").equalTo(productDescription);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                snapshot.getRef().removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                // Deletion successful
                                                Toast.makeText(requireContext(), "Product deleted", Toast.LENGTH_SHORT).show();
                                                // Optionally, navigate back or perform other actions
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Deletion failed
                                                Toast.makeText(requireContext(), "Failed to delete product", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                break; // Assuming the product is unique based on name and description
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Error occurred while querying the database
                            Toast.makeText(requireContext(), "Failed to delete product", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        populateProductInformation();
    }

    private void populateProductInformation() {
        if (product != null) {
            productNameTextView.setText(product.getName());
            descriptionTextView.setText(product.getDescription());
            environmentalEffectTextView.setText(product.getEnvironmentalEffects());
            ecoFriendlyTipsTextView.setText(product.getEcoFriendlyTips());
            alternativeProductsTextView.setText(product.getAlternativeProducts());

            // Load the product image using Glide
            Glide.with(requireContext())
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.product_image_placeholder)
                    .error(R.drawable.error_image)
                    .into(productImageView);
        }
    }
}
