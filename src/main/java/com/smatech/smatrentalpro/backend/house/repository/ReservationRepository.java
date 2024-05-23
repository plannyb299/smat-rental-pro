package com.smatech.smatrentalpro.backend.house.repository;

import com.smatech.smatrentalpro.backend.house.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {
    List<Reservation> findAll();
    Optional<Reservation> findById(String id);
}