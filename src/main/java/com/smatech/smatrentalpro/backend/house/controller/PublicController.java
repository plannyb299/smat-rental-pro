package com.smatech.smatrentalpro.backend.house.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smatech.smatrentalpro.backend.house.dto.request.HouseRequest;
import com.smatech.smatrentalpro.backend.house.dto.response.HouseResponse;
import com.smatech.smatrentalpro.backend.house.model.AllHomesList;
import com.smatech.smatrentalpro.backend.house.model.Category;
import com.smatech.smatrentalpro.backend.house.service.HostService;
import com.smatech.smatrentalpro.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.smatech.smatrentalpro.backend.utils.Helpers.convertToJson;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
public class PublicController {


    private final HostService hostService;

    private final UserService userService;



    @PostMapping("/home/new")
    public ResponseEntity<String> createHome(@RequestBody HouseRequest myHomePostDto) throws Exception {
//        User user = userService.findByUsername(principal.getName());
//        if(user.getRoles().stream().findFirst().isPresent()||user.getRoles().stream().findFirst().isEmpty())
            return ResponseEntity.ok().body(convertToJson(hostService.save(myHomePostDto)));
//        else
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"Status\": \"Error\"}");
    }
    @GetMapping("/homes/all")
    public ResponseEntity<List<HouseResponse>> getAllHomes()  throws JsonProcessingException {
        List<HouseResponse> response = hostService.findAll();
        return response != null
                ? new ResponseEntity<>(response, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/homes/byId/{id}")
    public ResponseEntity<HouseResponse> getHomeById(@PathVariable String id)  throws JsonProcessingException {
        HouseResponse house = hostService.findHomeById(id);
        return house != null
                ? new ResponseEntity<>(house, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/homes/byCategory/{category}")
    public ResponseEntity<List<HouseResponse>> getHomeByCategory(@PathVariable Category category)  throws JsonProcessingException {
        List<HouseResponse> house = hostService.findHomeCategory(category);
        return house != null
                ? new ResponseEntity<>(house, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/homes")
    public ResponseEntity<String> getHomesByFilter(
            @RequestParam String people,
            @RequestParam String latitude,
            @RequestParam String longitude,
            @RequestParam String arrivalDate,
            @RequestParam String departureDate
    ) throws JsonProcessingException, ParseException {
        if(people.isEmpty())people="0";
        if(latitude.isEmpty())latitude="0.0";
        if(longitude.isEmpty())longitude="0.0";
        if(arrivalDate.isEmpty())arrivalDate="1997-01-01";
        if(departureDate.isEmpty())departureDate="1997-01-01";
        Date arrivalDateConverted = new SimpleDateFormat("yyyy-MM-dd").parse(arrivalDate);
        Date departureDateConverted = new SimpleDateFormat("yyyy-MM-dd").parse(departureDate);

        return ResponseEntity.ok().body(convertToJson(hostService.findAllUsingFilters(
                Integer.parseInt(people),
                Double.parseDouble(latitude),
                Double.parseDouble(longitude),
                arrivalDateConverted,
                departureDateConverted
        )));
    }

    @GetMapping("/homeByPriceAndCity")
    public ResponseEntity<List<HouseResponse>> getHomesByPriceAndLocation(
            @RequestParam String minPrice,
            @RequestParam String maxPrice,
            @RequestParam @Nullable String city
    ) throws JsonProcessingException, ParseException {
        if(minPrice.isEmpty())minPrice="0.0";
        if(maxPrice.isEmpty())maxPrice="0.0";
        assert city != null;
        if(city.isEmpty())city="Harare";
        return ResponseEntity.ok().body(hostService.findAllWithCityAndPrice(
                city,
                minPrice,
                maxPrice
        ));
    }

    @PostMapping("/homes/more")
    public ResponseEntity<String> getHomesByMoreFilters(
            @RequestBody @Nullable AllHomesList allHomesList,
            @RequestParam(required = false) String maxPrice,
            @RequestParam(required = false) String minPrice,
            @RequestParam(required = false) Boolean wifi,
            @RequestParam(required = false) Boolean elevator,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Boolean kitchen,
            @RequestParam(required = false) Boolean parking,
            @RequestParam(required = false) Boolean tv,
            @RequestParam(required = false) Boolean ac,
            @RequestParam(required = false) String type
    ) throws JsonProcessingException {
        return ResponseEntity.ok().body(
                convertToJson(hostService.findAllUsingMoreFilters(
                        allHomesList,
                        maxPrice,
                        minPrice,
                        wifi,
                        elevator,
                        city,
                        kitchen,
                        parking,
                        tv,
                        ac,
                        type
                )));
    }

}
