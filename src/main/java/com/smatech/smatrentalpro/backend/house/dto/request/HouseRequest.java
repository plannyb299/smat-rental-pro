package com.smatech.smatrentalpro.backend.house.dto.request;

import com.smatech.smatrentalpro.backend.house.model.Category;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class  HouseRequest {

    private String street;
    private Integer rooms;
    private Integer bedrooms;
    private Integer bathrooms;
    private String shortAddress;
    private String image;
//    private List<ImageReq> images;
    private LocationReq location;
    @Enumerated(EnumType.STRING)
    private Category category;
    private Double price;
    private Double rent;
    private String description;
    private String neighbourhood;
    private FacilitiesReq facilities;

}
