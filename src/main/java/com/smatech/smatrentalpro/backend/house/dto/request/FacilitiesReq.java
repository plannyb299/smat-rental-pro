package com.smatech.smatrentalpro.backend.house.dto.request;

import lombok.Data;

@Data
public class FacilitiesReq {

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
