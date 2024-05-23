package com.smatech.smatrentalpro.backend.house.dto.request;


import lombok.Data;

@Data
public class ResidencyReq {

    private String title;
    private String description;
    private Double price;
    private String address;
    private String country;
    private String city;
    private String image;
    private FacilitiesReq facilities;
}
