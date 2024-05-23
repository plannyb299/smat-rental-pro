package com.smatech.smatrentalpro.backend.house.service;


import com.smatech.smatrentalpro.backend.house.dto.request.ReservationReq;
import com.smatech.smatrentalpro.backend.house.dto.response.ReservationRes;
import com.smatech.smatrentalpro.backend.house.model.Reservation;

import java.util.List;


public interface ReservationService {
    ReservationRes findReservationDtoById(String id);
    Reservation findReservationById(String id);
    List<ReservationRes> findAll();
    ReservationRes save(ReservationReq reservationDto);
}
