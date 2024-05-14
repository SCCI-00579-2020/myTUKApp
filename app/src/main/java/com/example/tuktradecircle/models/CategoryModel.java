package com.example.tuktradecircle.models;

public class CategoryModel {
    String img_url;
    String Name;
    String Type;

    public CategoryModel() {
    }

    public CategoryModel(String img_url, String name, String type) {
        this.img_url = img_url;
        Name = name;
        Type = type;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
