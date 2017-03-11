package com.shivshankar.classes;

import java.io.Serializable;

public class CartItem implements Serializable {

    private static final long serialVersionUID = 1L;
    private String OrderDetailId, cartId, ProductId, ColorId, insertedAt, sizevalue, ProductWeight;
    int quanitity, availQuantity, FulfillQty, ShippedRequestedQty;
    String Price, stitchingPrice, sizeCost, Amount, Vat;
    boolean OutOfStock, isCatalogItem;

    public CartItem(String orderDetailId, String cartId, String productId, String colorId, String insertedAt, int quanitity, String price, String stitchingPrice, String sizevalue, String sizeCost, String amount,
                   String v, int availquantity, boolean isOutOfStock, int fulfillQty, int shippedRequestedQty, String productWeight, boolean isCatalogitem) {
        super();
        OrderDetailId = orderDetailId;
        this.cartId = cartId;
        ProductId = productId;
        ColorId = colorId;
        this.insertedAt = insertedAt;
        this.quanitity = quanitity;
        Price = price;
        this.stitchingPrice = stitchingPrice;
        this.sizevalue = sizevalue;
        this.sizeCost = sizeCost;
        Amount = amount;
        Vat = v;
        availQuantity = availquantity;
        OutOfStock = isOutOfStock;
        FulfillQty = fulfillQty;
        ShippedRequestedQty = shippedRequestedQty;
        ProductWeight = productWeight;
        this.isCatalogItem = isCatalogitem;
    }

    public boolean isCatalogItem() {
        return isCatalogItem;
    }

    public void setCatalogItem(boolean catalogItem) {
        isCatalogItem = catalogItem;
    }

    public String getProductWeight() {
        return ProductWeight;
    }

    public void setProductWeight(String productWeight) {
        ProductWeight = productWeight;
    }


    public int getFulfillQty() {
        return FulfillQty;
    }

    public void setFulfillQty(int fulfillQty) {
        FulfillQty = fulfillQty;
    }

    public int getShippedRequestedQty() {
        return ShippedRequestedQty;
    }

    public void setShippedRequestedQty(int shippedRequestedQty) {
        ShippedRequestedQty = shippedRequestedQty;
    }

    public String getOrderDetailId() {
        return OrderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        OrderDetailId = orderDetailId;
    }

    public boolean isOutOfStock() {
        return OutOfStock;
    }

    public void setOutOfStock(boolean outOfStock) {
        OutOfStock = outOfStock;
    }

    public int getAvailQuantity() {
        return availQuantity;
    }

    public void setAvailQuantity(int availQuantity) {
        this.availQuantity = availQuantity;
    }

    public String getVat() {
        return Vat;
    }

    public void setVat(String vat) {
        Vat = vat;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getColorId() {
        return ColorId;
    }

    public void setColorId(String colorId) {
        ColorId = colorId;
    }

    public String getInsertedAt() {
        return insertedAt;
    }

    public void setInsertedAt(String insertedAt) {
        this.insertedAt = insertedAt;
    }

    public int getQuanitity() {
        return quanitity;
    }

    public void setQuanitity(int quanitity) {
        this.quanitity = quanitity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getStitchingPrice() {
        return stitchingPrice;
    }

    public void setStitchingPrice(String stitchingPrice) {
        this.stitchingPrice = stitchingPrice;
    }

    public String getSizevalue() {
        return sizevalue;
    }

    public void setSizevalue(String sizevalue) {
        this.sizevalue = sizevalue;
    }

    public String getSizeCost() {
        return sizeCost;
    }

    public void setSizeCost(String sizeCost) {
        this.sizeCost = sizeCost;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }


}
