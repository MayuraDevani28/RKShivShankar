package com.shivshankar.classes;

import java.io.Serializable;
import java.util.ArrayList;

public class CartItem implements Serializable {

    private static final long serialVersionUID = 1L;
    String CartId, ProductId, ProductCode, productName, BrandName, MarketPrice, OfferPrice, TotalAmount, DiscountPercent, ImageName, BodyPart, BodyFabricPart;
    ArrayList<SC3Object> Fabric_Colors;
    int CartQuantity, MinOrderQuantity, SuitFbricId, Fabric_FrontQty, Fabric_BackQty, Fabric_BajuQty, Fabric_ExtraQty;
    double FabricCuts, Fabric_FrontCut, Fabric_BackCut, Fabric_BajuCut, Fabric_ExtraCut;
    boolean IsOutOfStock, isExpanded;

    public CartItem(String cartId, String productId, String productCode, String productName, String brandName, String marketPrice, String offerPrice, String totalAmount, String discountPercent, String imageName, int cartQuantity, int minOrderQuantity, boolean isOutOfStock, boolean isExpanded, int suitFbricId, String bodyPart, String bodyFabricPart, double fabricCuts,
                    int fabric_FrontQty
            , double fabric_FrontCut
            , int fabric_BackQty
            , double fabric_BackCut
            , int fabric_BajuQty
            , double fabric_BajuCut
            , int fabric_ExtraQty
            , double fabric_ExtraCut
            , ArrayList<SC3Object> fabric_Colors) {
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
        SuitFbricId = suitFbricId;
        BodyPart = bodyPart;
        BodyFabricPart = bodyFabricPart;
        FabricCuts = fabricCuts;

        Fabric_FrontQty = fabric_FrontQty;
        Fabric_FrontCut = fabric_FrontCut;
        Fabric_BackQty = fabric_BackQty;
        Fabric_BackCut = fabric_BackCut;
        Fabric_BajuQty = fabric_BajuQty;
        Fabric_BajuCut = fabric_BajuCut;
        Fabric_ExtraQty = fabric_ExtraQty;
        Fabric_ExtraCut = fabric_ExtraCut;
        Fabric_Colors = fabric_Colors;

    }

    public double getFabric_ExtraCut() {
        return Fabric_ExtraCut;
    }

    public void setFabric_ExtraCut(double fabric_ExtraCut) {
        Fabric_ExtraCut = fabric_ExtraCut;
    }

    public double getFabric_BajuCut() {
        return Fabric_BajuCut;
    }

    public void setFabric_BajuCut(double fabric_BajuCut) {
        Fabric_BajuCut = fabric_BajuCut;
    }

    public double getFabric_BackCut() {
        return Fabric_BackCut;
    }

    public void setFabric_BackCut(double fabric_BackCut) {
        Fabric_BackCut = fabric_BackCut;
    }

    public double getFabric_FrontCut() {
        return Fabric_FrontCut;
    }

    public void setFabric_FrontCut(double fabric_FrontCut) {
        Fabric_FrontCut = fabric_FrontCut;
    }

    public int getFabric_ExtraQty() {
        return Fabric_ExtraQty;
    }

    public void setFabric_ExtraQty(int fabric_ExtraQty) {
        Fabric_ExtraQty = fabric_ExtraQty;
    }

    public int getFabric_BajuQty() {
        return Fabric_BajuQty;
    }

    public void setFabric_BajuQty(int fabric_BajuQty) {
        Fabric_BajuQty = fabric_BajuQty;
    }

    public int getFabric_BackQty() {
        return Fabric_BackQty;
    }

    public void setFabric_BackQty(int fabric_BackQty) {
        Fabric_BackQty = fabric_BackQty;
    }

    public int getFabric_FrontQty() {
        return Fabric_FrontQty;
    }

    public void setFabric_FrontQty(int fabric_FrontQty) {
        Fabric_FrontQty = fabric_FrontQty;
    }

    public ArrayList<SC3Object> getFabric_Colors() {
        return Fabric_Colors;
    }

    public void setFabric_Colors(ArrayList<SC3Object> fabric_Colors) {
        Fabric_Colors = fabric_Colors;
    }


    public String getBodyPart() {
        return BodyPart;
    }

    public void setBodyPart(String bodyPart) {
        BodyPart = bodyPart;
    }

    public String getBodyFabricPart() {
        return BodyFabricPart;
    }

    public void setBodyFabricPart(String bodyFabricPart) {
        BodyFabricPart = bodyFabricPart;
    }

    public double getFabricCuts() {
        return FabricCuts;
    }

    public void setFabricCuts(double fabricCuts) {
        FabricCuts = fabricCuts;
    }

    public int getSuitFbricId() {
        return SuitFbricId;
    }

    public void setSuitFbricId(int suitFbricId) {
        SuitFbricId = suitFbricId;
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
