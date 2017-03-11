package com.shivshankar.classes;

import java.io.Serializable;

public class CatalogItem implements Serializable {

    private static final long serialVersionUID = -3155132036171522826L;

    String CatalogId, CatalogCode, CatalogName, BrandName, ImageName, MarketPrice, OfferPrice, AverageMarketPrice, AverageOfferPrice;
    int DiscountPercent, TotalCatalogItem, Quantity;
    boolean IsHotDeals, IsOutOfStock, IsHasSingleProduct;

    public CatalogItem(String catalogId, String catalogCode, String catalogName, String brandName, String imageName, String marketPrice, String offerPrice, String averageMarketPrice, String averageOfferPrice, int discountPercent, int totalCatalogItem, int quantity, boolean isHotDeals, boolean isOutOfStock, boolean IshasSingleProduct) {
        CatalogId = catalogId;
        CatalogCode = catalogCode;
        CatalogName = catalogName;
        BrandName = brandName;
        ImageName = imageName;
        MarketPrice = marketPrice;
        OfferPrice = offerPrice;
        AverageMarketPrice = averageMarketPrice;
        AverageOfferPrice = averageOfferPrice;
        DiscountPercent = discountPercent;
        TotalCatalogItem = totalCatalogItem;
        Quantity = quantity;
        IsHotDeals = isHotDeals;
        IsOutOfStock = isOutOfStock;
        IsHasSingleProduct = IshasSingleProduct;
    }

    public boolean isHasSingleProduct() {
        return IsHasSingleProduct;
    }

    public void setHasSingleProduct(boolean hasSingleProduct) {
        IsHasSingleProduct = hasSingleProduct;
    }


    public String getAverageOfferPrice() {
        return AverageOfferPrice;
    }

    public void setAverageOfferPrice(String averageOfferPrice) {
        AverageOfferPrice = averageOfferPrice;
    }

    public boolean isOutOfStock() {
        return IsOutOfStock;
    }

    public void setOutOfStock(boolean outOfStock) {
        IsOutOfStock = outOfStock;
    }

    public boolean isHotDeals() {
        return IsHotDeals;
    }

    public void setHotDeals(boolean hotDeals) {
        IsHotDeals = hotDeals;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getCatalogId() {
        return CatalogId;
    }

    public void setCatalogId(String catalogId) {
        CatalogId = catalogId;
    }

    public String getCatalogCode() {
        return CatalogCode;
    }

    public void setCatalogCode(String catalogCode) {
        CatalogCode = catalogCode;
    }

    public String getCatalogName() {
        return CatalogName;
    }

    public void setCatalogName(String catalogName) {
        CatalogName = catalogName;
    }

    public String getBrandName() {
        return BrandName;
    }

    public void setBrandName(String brandName) {
        BrandName = brandName;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
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

    public String getAverageMarketPrice() {
        return AverageMarketPrice;
    }

    public void setAverageMarketPrice(String averageMarketPrice) {
        AverageMarketPrice = averageMarketPrice;
    }

    public int getDiscountPercent() {
        return DiscountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        DiscountPercent = discountPercent;
    }

    public int getTotalCatalogItem() {
        return TotalCatalogItem;
    }

    public void setTotalCatalogItem(int totalCatalogItem) {
        TotalCatalogItem = totalCatalogItem;
    }


}