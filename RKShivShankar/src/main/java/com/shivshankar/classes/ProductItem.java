package com.shivshankar.classes;

import java.io.Serializable;

public class ProductItem implements Serializable {

    private static final long serialVersionUID = -3155132036171522826L;

    String ProductId, ProductCode, OfferPrice, ImageName, FabricType, TopFabricId, TopFabricName, BottomFabricId, BottomFabricName, DupattaFabricId, DupattaFabricName, FabricId, FabricName, SellerId, SellerName, CategoryId, CategoryName;
    int MinOrderQty;
    boolean isExpanded, IsAllOver;
    Brand brand;

    public ProductItem(String productId, String productCode, String offerPrice, String imageName, String fabricType, String topFabricId, String topFabricName, String bottomFabricId, String bottomFabricName, String dupattaFabricId, String dupattaFabricName, String fabricId, String fabricName, String sellerId, String sellerName, String categoryId, String categoryName, int minOrderQty, boolean isExpanded, boolean isAllOver, Brand brand) {
        ProductId = productId;
        ProductCode = productCode;
        OfferPrice = offerPrice;
        ImageName = imageName;
        FabricType = fabricType;
        TopFabricId = topFabricId;
        TopFabricName = topFabricName;
        BottomFabricId = bottomFabricId;
        BottomFabricName = bottomFabricName;
        DupattaFabricId = dupattaFabricId;
        DupattaFabricName = dupattaFabricName;
        FabricId = fabricId;
        FabricName = fabricName;
        SellerId = sellerId;
        SellerName = sellerName;
        CategoryId = categoryId;
        CategoryName = categoryName;
        MinOrderQty = minOrderQty;
        this.isExpanded = isExpanded;
        IsAllOver = isAllOver;
        this.brand = brand;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public String getOfferPrice() {
        return OfferPrice;
    }

    public void setOfferPrice(String offerPrice) {
        OfferPrice = offerPrice;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getFabricType() {
        return FabricType;
    }

    public void setFabricType(String fabricType) {
        FabricType = fabricType;
    }

    public String getTopFabricId() {
        return TopFabricId;
    }

    public void setTopFabricId(String topFabricId) {
        TopFabricId = topFabricId;
    }

    public String getTopFabricName() {
        return TopFabricName;
    }

    public void setTopFabricName(String topFabricName) {
        TopFabricName = topFabricName;
    }

    public String getBottomFabricId() {
        return BottomFabricId;
    }

    public void setBottomFabricId(String bottomFabricId) {
        BottomFabricId = bottomFabricId;
    }

    public String getBottomFabricName() {
        return BottomFabricName;
    }

    public void setBottomFabricName(String bottomFabricName) {
        BottomFabricName = bottomFabricName;
    }

    public String getDupattaFabricId() {
        return DupattaFabricId;
    }

    public void setDupattaFabricId(String dupattaFabricId) {
        DupattaFabricId = dupattaFabricId;
    }

    public String getDupattaFabricName() {
        return DupattaFabricName;
    }

    public void setDupattaFabricName(String dupattaFabricName) {
        DupattaFabricName = dupattaFabricName;
    }

    public String getFabricId() {
        return FabricId;
    }

    public void setFabricId(String fabricId) {
        FabricId = fabricId;
    }

    public String getFabricName() {
        return FabricName;
    }

    public void setFabricName(String fabricName) {
        FabricName = fabricName;
    }

    public String getSellerId() {
        return SellerId;
    }

    public void setSellerId(String sellerId) {
        SellerId = sellerId;
    }

    public String getSellerName() {
        return SellerName;
    }

    public void setSellerName(String sellerName) {
        SellerName = sellerName;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public int getMinOrderQty() {
        return MinOrderQty;
    }

    public void setMinOrderQty(int minOrderQty) {
        MinOrderQty = minOrderQty;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public boolean isAllOver() {
        return IsAllOver;
    }

    public void setAllOver(boolean allOver) {
        IsAllOver = allOver;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

}