package com.smatech.smatrentalpro.backend.house.service;

import com.smatech.smatrentalpro.backend.house.dto.request.HouseRequest;
import com.smatech.smatrentalpro.backend.house.dto.response.HouseResponse;
import com.smatech.smatrentalpro.backend.house.dto.response.ReservationRes;
import com.smatech.smatrentalpro.backend.house.model.*;
import com.smatech.smatrentalpro.backend.house.processor.HouseProcessor;
import com.smatech.smatrentalpro.backend.house.repository.HouseRepository;
import com.smatech.smatrentalpro.backend.utils.Helpers;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class HostServiceImpl implements HostService {

    private final HouseProcessor houseProcessor;
    private final HouseRepository houseRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public House findHomeDtoById(String id) throws Exception {
        House house;
        try {
            house = houseRepository.findById(id).get();
        } catch (NoSuchElementException nsee) {
            throw new Exception("Report not found", nsee.getCause());
        }
        return house;
    }

    @Override
    public HouseResponse findHomeById(String id) {
       HouseResponse houseResponse = new HouseResponse();
        Optional<House> house = houseRepository.findById(id);
        if(house.isPresent()) {
            House house1= house.get();
           houseResponse = HouseProcessor.convertToDto(house1);
        }
        return houseResponse;
    }

    @Override
    public List<HouseResponse> findHomeCategory(Category category) {

        List<House> houseList = houseRepository.findByCategory(category);
        List<HouseResponse> response = new ArrayList<>();

        for(House hs :houseList){
        HouseResponse houseResponse = HouseProcessor.convertToDto(hs);
        response.add(houseResponse);
        }
        return response;
    }

    @Override
    public List<HouseResponse> findByUserId(Integer id) {
        List<HouseResponse> response = houseRepository.findByOwner_Id(id)
                .stream()
                .map(HouseProcessor::convertToDto)
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public List<HouseResponse> findByUsername(String username) {
        List<HouseResponse> response = houseRepository.findByOwner_Username(username)
                .stream()
                .map(HouseProcessor::convertToDto)
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public House findByAddress(String address) {
        House House;
        House = houseRepository.findByAddress(address).get();
        return House;
    }

    @Override
    public HouseResponse save(HouseRequest request) {

        House house = HouseProcessor.convert(request);

            log.info("House: {}", house);
            House home = houseRepository.save(house);

            return HouseProcessor.convertToDto(home);

    }

    @Override
    public HouseResponse saveUpdate(HouseRequest housePostDto) {
        House house = HouseProcessor.convert(housePostDto);

        Optional<House> tempHome = houseRepository.findByAddress(housePostDto.getLocation().getAddress());
        house.setId(tempHome.get().getId());

        House house1 = houseRepository.save(house);

        return houseProcessor.convertToDto(house1);
    }

    @Override
    public void deleteById(String id) {
        houseRepository.deleteById(id);
    }


    @Override
    public List<HouseResponse> findAll() {
        EntityGraph<House> graph = entityManager.createEntityGraph(House.class);
        graph.addAttributeNodes("location", "facilities");

        return entityManager.createQuery("SELECT h FROM House h", House.class)
                .setHint("javax.persistence.fetchgraph", graph)
                .getResultList()
                .stream()
                .map(HouseProcessor::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<HouseResponse> findAllWithLocationAndFacilities() {
        List<House> houseList = houseRepository.findAllWithLocationAndFacilities();
        log.info("Houses List : {}", houseList);

           return houseList.stream()
                .map(HouseProcessor::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AllHomesList findAllUsingFilters(int people, double latitude, double longitude, Date bookDate, Date leaveDate) {
        AllHomesList allHomesList = new AllHomesList();

        List<HouseResponse> tempListWithAllHomes = houseRepository.findAll()
                .stream()
                .map(HouseProcessor::convertToDto)
                .collect(Collectors.toList());


        //filter homes by distance by range search
        List<HouseResponse> filteredHomeListByDistance = filterHomeListByDistance(tempListWithAllHomes, latitude, longitude);


        //sort by price
        List<HouseResponse> sortedHomesByPrice = sortHomesByPrice(filteredHomeListByDistance);

        allHomesList.setHomes(sortedHomesByPrice);
        return allHomesList;
    }

    @Override
    public AllHomesList findAllUsingMoreFilters(AllHomesList allHomesList,
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
    ) {
        //filter homes by max price
        if (maxPrice != null) {
            allHomesList.setHomes(filterHomeListByMaxPrice(Double.parseDouble(maxPrice), allHomesList.getHomes()));
        }
        if (minPrice != null) {
            allHomesList.setHomes(filterHomeListByMinPrice(Double.parseDouble(minPrice), allHomesList.getHomes()));
        }
        if (wifi != null) {
            allHomesList.setHomes(filterHomeListByWifi(allHomesList.getHomes(), wifi));
        }
        if (elevator != null) {
            allHomesList.setHomes(filterHomeListByElevator(allHomesList.getHomes(), elevator));
        }
        if (kitchen != null) {
            allHomesList.setHomes(filterHomeListByKitchen(allHomesList.getHomes(), kitchen));
        }
        if (parking != null) {
            allHomesList.setHomes(filterHomeListByParking(allHomesList.getHomes(), parking));
        }
        if (tv != null) {
            allHomesList.setHomes(filterHomeListByTv(allHomesList.getHomes(), tv));
        }
        if (ac != null) {
            allHomesList.setHomes(filterHomeListByAc(allHomesList.getHomes(), ac));
        }
        if (type != null) {
            allHomesList.setHomes(filterHomeListByHomeType(allHomesList.getHomes(), type));
        }
        if (city != null) {
            allHomesList.setHomes(filterHomeListByHomeCity(allHomesList.getHomes(), city));
        }
        return allHomesList;
    }

    private List<HouseResponse> filterHomeListByHomeType(List<HouseResponse> tempListWithAllHomes, String homeTypeName) {
        return tempListWithAllHomes.stream()
                .filter(t -> false)
                .collect(Collectors.toList());
    }

    private List<HouseResponse> filterHomeListByHomeCity(List<HouseResponse> tempListWithAllHomes, String city) {
        return tempListWithAllHomes.stream()
                .filter(t -> t.getLocation().getCity().equals(city))
                .collect(Collectors.toList());
    }

    private List<HouseResponse> filterHomeListByAc(List<HouseResponse> tempListWithAllHomes, Boolean ac) {
        return tempListWithAllHomes.stream()
                .filter(t -> t.getFacilities().isAc() == ac)
                .collect(Collectors.toList());
    }

    private List<HouseResponse> filterHomeListByTv(List<HouseResponse> tempListWithAllHomes, Boolean tv) {
        return tempListWithAllHomes.stream()
                .filter(t -> t.getFacilities().isTv() == tv)
                .collect(Collectors.toList());
    }

    private List<HouseResponse> filterHomeListByParking(List<HouseResponse> tempListWithAllHomes, Boolean parking) {
        return tempListWithAllHomes.stream()
                .filter(t -> t.getFacilities().isParking() == parking)
                .collect(Collectors.toList());
    }

    private List<HouseResponse> filterHomeListByKitchen(List<HouseResponse> tempListWithAllHomes, Boolean kitchen) {
        return tempListWithAllHomes.stream()
                .filter(t -> t.getFacilities().isKitchen() == kitchen)
                .collect(Collectors.toList());
    }

    private List<House> filterHomeListByHeating(List<House> tempListWithAllHomes, Boolean heating) {
        return tempListWithAllHomes.stream()
                .filter(t -> t.getFacilities().isHeating() == heating)
                .collect(Collectors.toList());
    }

    private List<HouseResponse> filterHomeListByWifi(List<HouseResponse> tempListWithAllHomes, Boolean wifi) {
        return tempListWithAllHomes.stream()
                .filter(t -> t.getFacilities().isElevator() == wifi)
                .collect(Collectors.toList());
    }

    private List<HouseResponse> filterHomeListByElevator(List<HouseResponse> tempListWithAllHomes, Boolean elevator) {
        return tempListWithAllHomes.stream()
                .filter(t -> t.getFacilities().isElevator() == elevator)
                .collect(Collectors.toList());
    }

    private List<HouseResponse> filterHomeListByMaxPrice(Double maxPrice, List<HouseResponse> tempListWithAllHomes) {
        return tempListWithAllHomes.stream()
                .filter(t -> t.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    private List<HouseResponse> filterHomeListByMinPrice(Double minPrice, List<HouseResponse> tempListWithAllHomes) {
        return tempListWithAllHomes.stream()
                .filter(t -> t.getPrice() <= minPrice)
                .collect(Collectors.toList());
    }

    private List<HouseResponse> sortHomesByPrice(List<HouseResponse> tempListWithAllHomes) {
        return tempListWithAllHomes.stream()
                .sorted(Comparator.comparingDouble(HouseResponse::getPrice))
                .collect(Collectors.toList());
    }


    private List<House> filterHomeListByReservationDates(LocalDate imerominiaAfixis, LocalDate imerominiaAnaxwrisis, List<House> tempListWithAllHomes) {
        List<House> filteredList = new ArrayList<>();
        int einaiHImerominiaAfixisPrinTinImerominiaAfixisApoDB = 0;
        int einaiHImerominiaAfixisMetaTinImerominiaAnaxwrisisApoDB = 0;
        int einaiHImerominiaAnaxwrisisPrinTinImerominiaAfixisApoDB = 0;
        int einaiHImerominiaAnaxwrisisMetaTinImerominiaAnaxwrisisApoDB = 0;

        for (int i = 0; i < tempListWithAllHomes.size(); i++) {

            //an den iparxei kratisi gia to spiti tote mporei na ginei book opoiadhpote hmeromhnia
            if (tempListWithAllHomes.get(i).getReservations().isEmpty()) {
                filteredList.add(tempListWithAllHomes.get(i));
            }

            for (int j = 0; j < tempListWithAllHomes.get(i).getReservations().size(); j++) {
                einaiHImerominiaAfixisPrinTinImerominiaAfixisApoDB = checkBookingArrivalInReservations(imerominiaAfixis, tempListWithAllHomes.get(i).getReservations(), j);
                einaiHImerominiaAfixisMetaTinImerominiaAnaxwrisisApoDB = checkBookingLeaveInReservations(imerominiaAfixis, tempListWithAllHomes.get(i).getReservations(), j);

                einaiHImerominiaAnaxwrisisPrinTinImerominiaAfixisApoDB = checkBookingArrivalInReservations(imerominiaAnaxwrisis, tempListWithAllHomes.get(i).getReservations(), j);
                einaiHImerominiaAnaxwrisisMetaTinImerominiaAnaxwrisisApoDB = checkBookingLeaveInReservations(imerominiaAnaxwrisis, tempListWithAllHomes.get(i).getReservations(), j);
            }

            if ((einaiHImerominiaAfixisPrinTinImerominiaAfixisApoDB > 0 || einaiHImerominiaAfixisMetaTinImerominiaAnaxwrisisApoDB < 0) &&
                    (einaiHImerominiaAnaxwrisisPrinTinImerominiaAfixisApoDB > 0 || einaiHImerominiaAnaxwrisisMetaTinImerominiaAnaxwrisisApoDB < 0)) {
                filteredList.add(tempListWithAllHomes.get(i));
            }
        }
        return filteredList;
    }

    private int checkBookingArrivalInReservations(LocalDate bookDate, List<Reservation> reservationDtoList, int i) {
        return reservationDtoList
                .get(i)
                .getBookedDate()
                .compareTo(bookDate);
    }

    private int checkBookingLeaveInReservations(LocalDate bookDate, List<Reservation> reservationDtoList, int i) {
        return reservationDtoList
                .get(i)
                .getLeaveDate()
                .compareTo(bookDate);
    }


    private List<HouseResponse> filterHomeListByDistance(List<HouseResponse> homeList, double givenLat, double givenLong) {
        double maxDistance = 30; //kilometers
        List<HouseResponse> filteredHomes = homeList.stream()
                .map(home -> {
                    double distanceFromEachHome = Helpers.distance(Double.parseDouble(home.getLocation().getLatitude()), Double.parseDouble(home.getLocation().getLongitude()), givenLat, givenLong, "K");
                    System.out.println("distance Between visitor search and actual Home " + distanceFromEachHome);
                    if (distanceFromEachHome < maxDistance)
                        return home;
                    else
                        return null;
                })
                .collect(Collectors.toList());

        while (filteredHomes.remove(null)) ;

        if (filteredHomes.isEmpty() || filteredHomes == null)
            return Collections.emptyList();
        else
            return filteredHomes;
    }

    public HouseResponse updateHouse(String id, HouseRequest updatedHouse) {

        House existingHouse = houseRepository.findById(id).orElse(null);
        HouseResponse response = new HouseResponse();

        if (existingHouse != null) {
            existingHouse.setNeighbourhood(updatedHouse.getNeighbourhood());
            existingHouse.setStreet(updatedHouse.getStreet());
            existingHouse.setRooms(updatedHouse.getRooms());
            existingHouse.setBedrooms(updatedHouse.getBedrooms());
            existingHouse.setBathrooms(updatedHouse.getBathrooms());
            existingHouse.setShortAddress(updatedHouse.getShortAddress());
            existingHouse.setPrice(updatedHouse.getPrice());
            existingHouse.setRent(updatedHouse.getRent());
            existingHouse.setDescription(updatedHouse.getDescription());

            House upDatedHouse = houseRepository.save(existingHouse);
            BeanUtils.copyProperties(upDatedHouse,response);

            return response;
        }

        return null;
    }

    public void deleteHouse(String id) {
        houseRepository.deleteById(id);
    }

    @Override
    public Reviews getHomeReviews(String id) throws Exception {
        Reviews reviews = new Reviews();
        reviews.setReviews(new ArrayList<>());

        HouseResponse house = findHomeById(id);

        house.getReservations().forEach(t -> {
            if (t.getHomeReviewStars() != null) {
                reviews.getReviews().add(t.getHomeReviewStars());
            }
        });

        reviews.setTotalReviews(house.getReservations().stream().filter(r -> r.getHomeReviewStars() != null).count());

        house.getReservations().stream()
                .mapToInt(ReservationRes::getHomeReviewStars)
                .average()
                .ifPresent(reviews::setAverage);

        return reviews;
    }
}
