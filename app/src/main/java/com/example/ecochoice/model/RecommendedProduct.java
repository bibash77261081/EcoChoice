package com.example.ecochoice.model;

public class RecommendedProduct {
    private String name;
    private String description;
    private String image;
    private String link;
    private String category;;

    public RecommendedProduct() {}

    public RecommendedProduct(String name, String description, String image, String link, String category) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.link = link;
        this.category = category;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

