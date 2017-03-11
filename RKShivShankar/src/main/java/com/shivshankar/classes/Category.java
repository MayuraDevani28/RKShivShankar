package com.shivshankar.classes;

import java.io.Serializable;

public class Category implements Serializable {

    private String id;
    private String name;
    private String parentCategoryID;
    private String ImageURL;
    private String filterString;

    boolean HasSubCategory;

    public Category(String parentCategoryID, String id, String name, String imageURL, boolean hasSubCategory, String filterString) {
        super();
        this.id = id;
        this.name = name;
        this.parentCategoryID = parentCategoryID;
        HasSubCategory = hasSubCategory;
        ImageURL = imageURL;
        this.filterString = filterString;
    }

    public Category(String id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public String getFilterString() {
        return filterString;
    }

    public void setFilterString(String filterString) {
        this.filterString = filterString;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public boolean HasSubCategory() {
        return HasSubCategory;
    }

    public void HasSubCategory(boolean hasSubCategory) {
        HasSubCategory = hasSubCategory;
    }

    public String getParentCategoryID() {
        return parentCategoryID;
    }

    public void setParentCategoryID(String parentCategoryID) {
        this.parentCategoryID = parentCategoryID;
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
}
