package com.shivshankar.classes;

/**
 * Created by Mayura on 4/22/2017.
 */

public class FabricColor {

    int ColorId;
    String ProductId, ColorName, HeaxCode, ColorImage;
    boolean isActive;

    public FabricColor(int colorId, String productId, String colorName, String heaxCode, String colorImage, boolean isactive) {
        ColorId = colorId;
        ProductId = productId;
        ColorName = colorName;
        HeaxCode = heaxCode;
        ColorImage = colorImage;
        isActive = isactive;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getColorId() {
        return ColorId;
    }

    public void setColorId(int colorId) {
        ColorId = colorId;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getColorName() {
        return ColorName;
    }

    public void setColorName(String colorName) {
        ColorName = colorName;
    }

    public String getHeaxCode() {
        return HeaxCode;
    }

    public void setHeaxCode(String heaxCode) {
        HeaxCode = heaxCode;
    }

    public String getColorImage() {
        return ColorImage;
    }

    public void setColorImage(String colorImage) {
        ColorImage = colorImage;
    }

}
