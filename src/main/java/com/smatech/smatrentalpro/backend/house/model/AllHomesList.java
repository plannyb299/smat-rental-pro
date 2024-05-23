package com.smatech.smatrentalpro.backend.house.model;



import com.smatech.smatrentalpro.backend.house.dto.response.HouseResponse;

import java.util.ArrayList;
import java.util.List;

public class AllHomesList {
    private List<HouseResponse> homes;

    public AllHomesList(){
        homes = new ArrayList<HouseResponse>();
    }

    public void add(HouseResponse home){
        homes.add(home);
    }

    public List<HouseResponse> getHomes() {
        return homes;
    }

    public void setHomes(List<HouseResponse> homes) {
        this.homes = homes;
    }
}
