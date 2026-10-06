package com.aisys.library.integration;

public interface Sip2Adapter {
    boolean checkoutItem(String patronBarcode, String itemBarcode);
    boolean checkinItem(String itemBarcode);
}