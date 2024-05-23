package com.smatech.smatrentalpro.backend.house.repository;


import com.smatech.smatrentalpro.backend.house.model.Category;
import com.smatech.smatrentalpro.backend.house.model.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HouseRepository extends JpaRepository<House, String> {

    Optional<House> findByAddress(String address);

//    List<House> findByOwnerUserId(Integer ownerId);

    List<House> findByOwner_Id(Integer id);

    List<House> findByOwner_Username(String username);


    @Query("select h from House h left join fetch h.location left join fetch h.facilities")
    List<House> findAllWithLocationAndFacilities();

    List<House> findByCategory(Category category);


}