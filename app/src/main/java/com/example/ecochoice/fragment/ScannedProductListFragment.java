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

import com.example.ecochoice.MainActivity;
import com.example.ecochoice.R;
import com.example.ecochoice.adapter.ProductAdapter;
import com.example.ecochoice.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ScannedProductListFragment extends Fragment implements ProductAdapter.OnItemClickListener {

    private DatabaseReference productsRef;
    private RecyclerView recyclerView;
    private ProductAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanned_product_list, container, false);

        // Initialize Firebase database reference
        productsRef = FirebaseDatabase.getInstance().getReference().child("products");

        // Initialize RecyclerView and its adapter
        recyclerView = view.findViewById(R.id.productRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProductAdapter();
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        // Retrieve product data from Firebase
        fetchProductsFromDatabase();

        return view;
    }

    private void fetchProductsFromDatabase() {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Product> productList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    productList.add(product);
                }
                adapter.setProducts(productList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to retrieve products", Toast.LENGTH_SHORT).show();
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



