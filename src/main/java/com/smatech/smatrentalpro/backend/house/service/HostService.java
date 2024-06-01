package com.smatech.smatrentalpro.backend.house.service;




import com.smatech.smatrentalpro.backend.house.dto.request.HouseRequest;
import com.smatech.smatrentalpro.backend.house.dto.response.HouseResponse;
import com.smatech.smatrentalpro.backend.house.model.AllHomesList;
import com.smatech.smatrentalpro.backend.house.model.Category;
import com.smatech.smatrentalpro.backend.house.model.House;
import com.smatech.smatrentalpro.backend.house.model.Reviews;

import java.util.Date;
import java.util.List;


public interface HostService {
    House findHomeDtoById(String id) throws Exception;

    HouseResponse findHomeById(String id);

    List<HouseResponse> findAll();

    List<HouseResponse> findAllWithLocationAndFacilities();

    List<HouseResponse> findHomeCategory(Category category);

    House findByAddress(String address);

    AllHomesList findAllUsingFilters(int people,
                                     double latitude,
                                     double longitude,
                                     Date bookDate,
                                     Date leaveDate);

    List<HouseResponse> findAllWithCityAndPrice(String city, String minPrice, String maxPrice);

    AllHomesList findAllUsingMoreFilters(AllHomesList allHomesList,
                                         String maxPrice,
                                         String minPrice,
                                         Boolean wifi,
                                         Boolean elevator,
                                         String city,
                                         Boolean kitchen,
                                         Boolean parking,
                                         Boolean tv,
                                         Boolean ac,
                                         String type
    );

    List<HouseResponse> findByUserId(Integer id);
    List<HouseResponse> findByUsername(String username);
//    HouseResponse save(HouseResponse HouseResponse);
    HouseResponse save(HouseRequest HousePostDto);

    HouseResponse saveUpdate(HouseRequest HousePostDto);

    void deleteById(String id);

    Reviews getHomeReviews(String id) throws Exception;
}
