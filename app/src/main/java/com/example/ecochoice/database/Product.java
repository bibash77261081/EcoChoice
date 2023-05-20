package com.example.ecochoice.database;

public class Product {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private double environmentalImpact;
    private String ecoFriendlyTip;

    public Product() {
        //default constructor
    }

    public Product(String id, String name, String description, String imageUrl, double environmentalImpact, String ecoFriendlyTip) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.environmentalImpact = environmentalImpact;
        this.ecoFriendlyTip = ecoFriendlyTip;
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

    public double getEnvironmentalImpact() {
        return environmentalImpact;
    }

    public void setEnvironmentalImpact(double environmentalImpact) {
        this.environmentalImpact = environmentalImpact;
    }

    public String getEcoFriendlyTip() {
        return ecoFriendlyTip;
    }

    public void setEcoFriendlyTip(String ecoFriendlyTip) {
        this.ecoFriendlyTip = ecoFriendlyTip;
    }
}
