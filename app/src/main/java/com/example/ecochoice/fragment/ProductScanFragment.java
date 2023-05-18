package com.example.ecochoice.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecochoice.R;

public class ProductScanFragment extends Fragment {
    public ProductScanFragment() {
        // Required empty public constructor
    }

    public static ProductScanFragment newInstance(String param1, String param2) {
        ProductScanFragment fragment = new ProductScanFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_scan, container, false);
    }
}