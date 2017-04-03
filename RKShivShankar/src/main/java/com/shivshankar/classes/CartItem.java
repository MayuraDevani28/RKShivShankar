package com.shivshankar.classes;

import java.io.Serializable;

public class CartItem implements Serializable {

    private static final long serialVersionUID = 1L;
    String CartId, ProductId, ProductCode, productName, BrandName, MarketPrice, OfferPrice, TotalAmount, DiscountPercent, ImageName;
    int CartQuantity, MinOrderQuantity;
    boolean IsOutOfStock, isExpanded;

    public CartItem(String cartId, String productId, String productCode, String productName, String brandName, String marketPrice, String offerPrice, String totalAmount, String discountPercent, String imageName, int cartQuantity, int minOrderQuantity, boolean isOutOfStock, boolean isExpanded) {
        CartId = cartId;
        ProductId = productId;
        ProductCode = productCode;
        this.productName = productName;
        BrandName = brandName;
        MarketPrice = marketPrice;
        OfferPrice = offerPrice;
        DiscountPercent = discountPercent;
        TotalAmount = totalAmount;
        ImageName = imageName;
        CartQuantity = cartQuantity;
        MinOrderQuantity = minOrderQuantity;
        IsOutOfStock = isOutOfStock;
        this.isExpanded = isExpanded;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.TotalAmount = totalAmount;
    }

    public String getCartId() {
        return CartId;
    }

    public void setCartId(String cartId) {
        CartId = cartId;
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
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBrandName() {
        return BrandName;
    }

    public void setBrandName(String brandName) {
        BrandName = brandName;
    }

    public String getMarketPrice() {
        return MarketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        MarketPrice = marketPrice;
    }

    public String getOfferPrice() {
        return OfferPrice;
    }

    public void setOfferPrice(String offerPrice) {
        OfferPrice = offerPrice;
    }

    public String getDiscountPercent() {
        return DiscountPercent;
    }

    public void setDiscountPercent(String discountPercent) {
        DiscountPercent = discountPercent;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public int getCartQuantity() {
        return CartQuantity;
    }

    public void setCartQuantity(int cartQuantity) {
        CartQuantity = cartQuantity;
    }

    public int getMinOrderQuantity() {
        return MinOrderQuantity;
    }

    public void setMinOrderQuantity(int minOrderQuantity) {
        MinOrderQuantity = minOrderQuantity;
    }

    public boolean isOutOfStock() {
        return IsOutOfStock;
    }

    public void setOutOfStock(boolean outOfStock) {
        IsOutOfStock = outOfStock;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

}
