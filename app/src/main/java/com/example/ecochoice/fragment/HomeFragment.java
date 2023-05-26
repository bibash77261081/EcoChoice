package com.example.ecochoice.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ecochoice.adapter.SlideAdapter;
import com.example.ecochoice.model.RecommendedProduct;
import com.example.ecochoice.R;
import com.example.ecochoice.adapter.RecommendedProductsAdapter;
import com.example.ecochoice.model.SlideItem;
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
    private ViewPager2 viewPager2;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager2 = view.findViewById(R.id.viewPager);
        recommendedProductsRecyclerView = view.findViewById(R.id.recommendedProductsRecyclerView);

        setupSlideImage();
        setupRecommendedProducts();

        return view;
    }

    private void setupSlideImage() {
        List<SlideItem> slideItems = new ArrayList<>();
        slideItems.add(new SlideItem(R.drawable.img1));
        slideItems.add(new SlideItem(R.drawable.img3));
        slideItems.add(new SlideItem(R.drawable.img2));
        slideItems.add(new SlideItem(R.drawable.img4));
        slideItems.add(new SlideItem(R.drawable.img5));

        viewPager2.setAdapter(new SlideAdapter(slideItems, viewPager2));
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
                RecommendedProductsAdapter adapter = new RecommendedProductsAdapter(recommendedProductsList, getContext());
                recommendedProductsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                recommendedProductsRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error case if the data retrieval fails
                Toast.makeText(getContext(), "Failed to get the products", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
