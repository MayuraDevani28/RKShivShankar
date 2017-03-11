package com.shivshankar.classes;

import java.io.Serializable;

public class ProductItem implements Serializable {

    private static final long serialVersionUID = -3155132036171522826L;

    String ProductId, ProductCode,
            ProductName,
            ProductDesignNo,
            Description,
            ImageName, MarketPrice, OfferPrice;
    int DiscountPercent;
    boolean isSelected, isSelectable;

    public ProductItem(String productId, String productCode, String productName, String productDesignNo, String description, String imageName, String marketPrice, String offerPrice, int discountPercent, boolean isSelected, boolean isSelectable) {
        ProductId = productId;
        ProductCode = productCode;
        ProductName = productName;
        ProductDesignNo = productDesignNo;
        Description = description;
        ImageName = imageName;
        MarketPrice = marketPrice;
        OfferPrice = offerPrice;
        DiscountPercent = discountPercent;
        this.isSelected = isSelected;
        this.isSelectable = isSelectable;
    }

    public boolean isSelectable() {
        return isSelectable;
    }

    public void setSelectable(boolean selectable) {
        isSelectable = selectable;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public String getMarketPrice() {
        return MarketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        MarketPrice = marketPrice;
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

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductDesignNo() {
        return ProductDesignNo;
    }

    public void setProductDesignNo(String productDesignNo) {
        ProductDesignNo = productDesignNo;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getOfferPrice() {
        return OfferPrice;
    }

    public void setOfferPrice(String offerPrice) {
        OfferPrice = offerPrice;
    }

    public int getDiscountPercent() {
        return DiscountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        DiscountPercent = discountPercent;
    }

}