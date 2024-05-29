package com.smatech.smatrentalpro.backend.house.dto.response;

import com.smatech.smatrentalpro.backend.house.model.Category;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.util.List;

@Data
public class HouseResponse {

    private String id;
    private String street;
    private Integer rooms;
    private Integer bedrooms;
    private Integer bathrooms;
    private String shortAddress;
    private String image;
    private List<ImageRes> images;
    private LocationRes location;
    @Enumerated(EnumType.STRING)
    private Category category;
    private Double price;
    private Double rent;
    private String description;
    private String neighbourhood;
    private FacilitiesRes facilities;
    private List<ReservationRes> reservations;
}
