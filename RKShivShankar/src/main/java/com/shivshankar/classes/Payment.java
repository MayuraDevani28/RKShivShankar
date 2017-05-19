package com.shivshankar.classes;

import java.io.Serializable;

/**
 * Created by Mayura on 3/16/2017.
 */

public class Payment implements Serializable {

    String PaymentOptionKey,PaymentOptionName, PaymentOptionDetails;

    public Payment(String paymentOptionKey, String paymentOptionName, String paymentOptionDetails) {
        PaymentOptionKey = paymentOptionKey;
        PaymentOptionName = paymentOptionName;
        PaymentOptionDetails = paymentOptionDetails;
    }

    public String getPaymentOptionKey() {
        return PaymentOptionKey;
    }

    public void setPaymentOptionKey(String paymentOptionKey) {
        PaymentOptionKey = paymentOptionKey;
    }

    public String getPaymentOptionName() {
        return PaymentOptionName;
    }

    public void setPaymentOptionName(String paymentOptionName) {
        PaymentOptionName = paymentOptionName;
    }

    public String getPaymentOptionDetails() {
        return PaymentOptionDetails;
    }

    public void setPaymentOptionDetails(String paymentOptionDetails) {
        PaymentOptionDetails = paymentOptionDetails;
    }
}
