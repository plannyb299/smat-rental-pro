package com.smatech.smatrentalpro.backend.house.dto.response;

import lombok.Data;

@Data
public class ResidencyRes {

    private String title;
    private String description;
    private Double price;
    private String address;
    private String country;
    private String city;
    private String image;
    private FacilitiesRes facilities;
}
