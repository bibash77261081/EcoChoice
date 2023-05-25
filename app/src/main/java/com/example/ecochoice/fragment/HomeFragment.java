package com.example.ecochoice.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecochoice.model.RecommendedProduct;
import com.example.ecochoice.R;
import com.example.ecochoice.adapter.RecommendedProductsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recommendedProductsRecyclerView;

    private List<RecommendedProduct> recommendedProductsList;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recommendedProductsRecyclerView = view.findViewById(R.id.recommendedProductsRecyclerView);

        setupRecommendedProducts();

        return view;
    }

    private void setupRecommendedProducts() {
        // Initialize the recommended products list
        recommendedProductsList = new ArrayList<>();

        // Retrieve recommended products from Firebase Realtime Database
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("recommended_products");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recommendedProductsList.clear();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    RecommendedProduct recommendedProduct = productSnapshot.getValue(RecommendedProduct.class);
                    recommendedProductsList.add(recommendedProduct);
                }

                // Set up the RecyclerView with the recommended products list
                RecommendedProductsAdapter adapter = new RecommendedProductsAdapter(recommendedProductsList);
                recommendedProductsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recommendedProductsRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error case if the data retrieval fails
                // You can show a Toast message or display an error UI
            }
        });
    }
}
