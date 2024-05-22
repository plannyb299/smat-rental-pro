package com.smatech.smatrentalpro.backend.security.models;


public class SmatRentalApiResp {

    private String message;

    public SmatRentalApiResp(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
