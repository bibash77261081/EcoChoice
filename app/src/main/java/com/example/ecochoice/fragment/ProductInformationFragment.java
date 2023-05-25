package com.example.ecochoice.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ecochoice.R;
import com.example.ecochoice.model.Product;

import java.io.Serializable;

public class ProductInformationFragment extends Fragment {
    private static final String ARG_PRODUCT = "product";

    private TextView productNameTextView;
    private TextView descriptionTextView;
    private ImageView productImageView;
    private TextView environmentalEffectTextView;
    private TextView ecoFriendlyTipsTextView;
    private TextView alternativeProductsTextView;

    private Product product;

    public ProductInformationFragment() {
        // Required empty public constructor
    }

    public static ProductInformationFragment newInstance(Product product) {
        ProductInformationFragment fragment = new ProductInformationFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, (Serializable) product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = (Product) getArguments().getSerializable(ARG_PRODUCT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_information, container, false);

        productNameTextView = view.findViewById(R.id.productNameTextView);
        descriptionTextView = view.findViewById(R.id.productDescriptionTextView);
        productImageView = view.findViewById(R.id.productImageView);
        environmentalEffectTextView = view.findViewById(R.id.environmentalEffectTextView);
        ecoFriendlyTipsTextView = view.findViewById(R.id.ecoFriendlyTipsTextView);
        alternativeProductsTextView = view.findViewById(R.id.alternativeProductsTextView);

        displayProductInformation();

        return view;
    }

    private void displayProductInformation() {
        if (product != null) {
            productNameTextView.setText(product.getName());
            descriptionTextView.setText(product.getDescription());
            // Load the product image using Glide or any other image loading library
            Glide.with(requireContext())
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.product_image_placeholder)
                    .error(R.drawable.error_image)
                    .into(productImageView);
            environmentalEffectTextView.setText(product.getEnvironmentalEffects());
            ecoFriendlyTipsTextView.setText(product.getEcoFriendlyTips());
            alternativeProductsTextView.setText(product.getAlternativeProducts());
        }
    }
}

