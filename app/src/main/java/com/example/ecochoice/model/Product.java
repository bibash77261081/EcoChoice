package com.example.ecochoice.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private String name;
    private String description;
    private String imageUrl;
    private String environmentalEffects;
    private String ecoFriendlyTips;
    private String alternativeProducts;

    public Product() {
        //default constructor
    }

    public Product(String name, String description, String imageUrl, String environmentalEffects, String ecoFriendlyTips, String alternativeProducts) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.environmentalEffects = environmentalEffects;
        this.ecoFriendlyTips = ecoFriendlyTips;
        this.alternativeProducts = alternativeProducts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEnvironmentalEffects() {
        return environmentalEffects;
    }

    public void setEnvironmentalEffects(String environmentalEffects) {
        this.environmentalEffects = environmentalEffects;
    }

    public String getEcoFriendlyTips() {
        return ecoFriendlyTips;
    }

    public void setEcoFriendlyTips(String ecoFriendlyTips) {
        this.ecoFriendlyTips = ecoFriendlyTips;
    }

    public String getAlternativeProducts() {
        return alternativeProducts;
    }

    public void setAlternativeProducts(String alternativeProducts) {
        this.alternativeProducts = alternativeProducts;
    }

    // Parcelable implementation
    protected Product(Parcel in) {
        name = in.readString();
        description = in.readString();
        imageUrl = in.readString();
        environmentalEffects = in.readString();
        ecoFriendlyTips = in.readString();
        alternativeProducts = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(imageUrl);
        dest.writeString(environmentalEffects);
        dest.writeString(ecoFriendlyTips);
        dest.writeString(alternativeProducts);
    }
}
