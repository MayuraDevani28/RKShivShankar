package com.shivshankar.classes;

/**
 * Created by Mayura on 3/16/2017.
 */

public class Brand {

    String brandId;
    String brandName, brandLogo;

    public Brand(String brandId, String brandName, String brandLogo) {
        this.brandId = brandId;
        this.brandName = brandName;
        this.brandLogo = brandLogo;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandLogo() {
        return brandLogo;
    }

    public void setBrandLogo(String brandLogo) {
        this.brandLogo = brandLogo;
    }
}
