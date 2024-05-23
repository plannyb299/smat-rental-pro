package com.smatech.smatrentalpro.backend.house.processor;

import com.smatech.smatrentalpro.backend.house.dto.request.HouseRequest;
import com.smatech.smatrentalpro.backend.house.dto.response.FacilitiesRes;
import com.smatech.smatrentalpro.backend.house.dto.response.HouseResponse;
import com.smatech.smatrentalpro.backend.house.dto.response.LocationRes;
import com.smatech.smatrentalpro.backend.house.model.Facilities;
import com.smatech.smatrentalpro.backend.house.model.House;
import com.smatech.smatrentalpro.backend.house.model.Location;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;


@Data
@RequiredArgsConstructor
@Component
@Slf4j
public class HouseProcessor {


    public static HouseResponse convertToDto(House house){

        HouseResponse response = new HouseResponse();

        LocationRes locationRes = new LocationRes();
        Location location1 = house.getLocation();

        BeanUtils.copyProperties(location1, locationRes);

        Facilities facilities = house.getFacilities();
        FacilitiesRes facilitiesRes = new FacilitiesRes();
        BeanUtils.copyProperties(facilities, facilitiesRes);

        BeanUtils.copyProperties(house, response);
        response.setLocation(locationRes);
        response.setFacilities(facilitiesRes);

        return response;
    }

    public static House convert(HouseRequest request){

        House house = new House();
        BeanUtils.copyProperties(request, house);
        Location location = new Location();
        BeanUtils.copyProperties(request.getLocation(),location);
        house.setLocation(location);
        Facilities facilities = new Facilities();
        BeanUtils.copyProperties(request.getFacilities(),facilities);
        house.setFacilities(facilities);

        return house;
    }

    public static House convertHouseRes(HouseResponse request){

        House house = new House();
        BeanUtils.copyProperties(request, house);
        
        Location location = new Location();
        BeanUtils.copyProperties(request.getLocation(),location);
        house.setLocation(location);

        Facilities facilities = new Facilities();
        BeanUtils.copyProperties(request.getFacilities(),facilities);
        house.setFacilities(facilities);

        return house;
    }

}
