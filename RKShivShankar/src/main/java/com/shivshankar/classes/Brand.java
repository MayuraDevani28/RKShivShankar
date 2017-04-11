package com.shivshankar.classes;

import java.io.Serializable;

/**
 * Created by Mayura on 3/16/2017.
 */

public class Brand implements Serializable {

    String brandId,brandName, brandLogo;

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
