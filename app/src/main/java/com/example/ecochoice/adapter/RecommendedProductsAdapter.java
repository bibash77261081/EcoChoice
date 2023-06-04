package com.example.ecochoice.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecochoice.R;
import com.example.ecochoice.model.RecommendedProduct;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class RecommendedProductsAdapter extends RecyclerView.Adapter<RecommendedProductsAdapter.ViewHolder> {
    private List<RecommendedProduct> recommendedProductsList;
    private Context context;

    // Constructor
    public RecommendedProductsAdapter(List<RecommendedProduct> recommendedProductsList, Context context) {
        this.recommendedProductsList = recommendedProductsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommended_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecommendedProduct recommendedProduct = recommendedProductsList.get(position);
        Log.d("image", recommendedProduct.getImage());
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(recommendedProduct.getImage().trim());

        holder.productNameTextView.setText(recommendedProduct.getName());
        holder.productDescriptionTextView.setText(recommendedProduct.getDescription());
        holder.productCategory.setText(recommendedProduct.getCategory());

        // Retrieve the download URL
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Use the downloadable URL with Glide
                String imageUrl = uri.toString();
                // Load the image with Glide using the imageUrl
                Glide.with(context)
                        .load(imageUrl)
                        .into(holder.productImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors that occurred while retrieving the downloadable URL
                Log.e("Glide", "Failed to get the downloadable URL: " + e.getMessage());
            }
        });

        holder.productNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an implicit intent to open the web page
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(recommendedProduct.getLink().trim()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recommendedProductsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productNameTextView;
        private TextView productDescriptionTextView;
        private TextView productCategory;
        private ImageView productImage;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImageView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productDescriptionTextView = itemView.findViewById(R.id.productDescriptionTextView);
            productCategory = itemView.findViewById(R.id.productCategory);
        }
    }
}
