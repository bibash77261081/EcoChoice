package com.example.ecochoice.Model;

public class Product {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private String environmentalEffects;
    private String ecoFriendlyTips;
    private String alternativeProducts;

    public Product() {
        //default constructor
    }

    public Product(String id, String name, String description, String imageUrl, String environmentalEffects, String ecoFriendlyTips, String alternativeProducts) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.environmentalEffects = environmentalEffects;
        this.ecoFriendlyTips = ecoFriendlyTips;
        this.alternativeProducts = alternativeProducts;
    }

    public Product(String name, String description, String imageUrl, String environmentalEffects, String ecoFriendlyTips, String alternativeProducts) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.environmentalEffects = environmentalEffects;
        this.ecoFriendlyTips = ecoFriendlyTips;
        this.alternativeProducts = alternativeProducts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
