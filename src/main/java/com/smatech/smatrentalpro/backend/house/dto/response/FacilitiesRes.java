package com.smatech.smatrentalpro.backend.house.dto.response;

import lombok.Data;

@Data
public class FacilitiesRes {

    private String bathrooms;
    private String bedrooms;
    private boolean ac;
    private boolean tv;
    private boolean parking;
    private boolean kitchen;
    private boolean heating;
    private boolean electricity;
    private boolean wifi;
    private boolean elevator;
}
