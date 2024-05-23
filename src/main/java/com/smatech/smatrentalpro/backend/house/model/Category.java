package com.smatech.smatrentalpro.backend.house.model;



public enum Category{

    SINGLE_FAMILY_HOME("Single Family Home"),
    HOUSE("House"),
    APARTMENT("Apartment"),

    STAND("Stand"),
    CONDO("Condo"),
    TOWNHOUSE("Townhouse"),
    DUPLEX("Duplex"),
    VILLA("Villa"),
    COTTAGE("Cottage"),
    BUNGALOW("Bungalow"),
    MANSION("Mansion"),
    TINY_HOUSE("Tiny House");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}