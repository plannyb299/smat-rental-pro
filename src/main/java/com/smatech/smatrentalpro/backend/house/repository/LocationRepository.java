package com.smatech.smatrentalpro.backend.house.repository;

import com.smatech.smatrentalpro.backend.house.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location,Long> {

}
