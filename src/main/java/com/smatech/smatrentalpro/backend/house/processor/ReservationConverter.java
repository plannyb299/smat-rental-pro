package com.smatech.smatrentalpro.backend.house.processor;


import com.smatech.smatrentalpro.backend.house.dto.request.ReservationReq;
import com.smatech.smatrentalpro.backend.house.dto.response.ReservationRes;
import com.smatech.smatrentalpro.backend.house.model.Reservation;
import com.smatech.smatrentalpro.backend.house.service.HostService;
import com.smatech.smatrentalpro.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationConverter {

    private final HostService hostService;

    private final UserService userService;

    private static HostService hostServiceStatic;

    private static UserService userServiceStatic;

    @Autowired
    public void setStatic() {
        this.hostServiceStatic = hostService;
        this.userServiceStatic = userService;
    }


    public static ReservationRes convertToDto(Reservation reservation) {
        ReservationRes reservationDto = new ReservationRes();
        reservationDto.setReservationId(reservation.getReservationId());
        reservationDto.setBookedDate(reservation.getBookedDate());
        reservationDto.setBookedHomeId(reservation.getBookedHome());
        reservationDto.setUserIdBooked(reservation.getUserBooked());
        reservationDto.setUserNameBooked(userServiceStatic.findById(reservation.getUserBooked()).getUsername());

        reservationDto.setHomeReviewDescription(reservation.getHomeReviewDescription());
        if(reservation.getHomeReviewStars()==null)reservationDto.setHomeReviewStars(0);
        else reservationDto.setHomeReviewStars(reservation.getHomeReviewStars());

        reservationDto.setHostReviewDescription(reservation.getHostReviewDescription());

        if(reservation.getHostReviewStars()==null)reservationDto.setHostReviewStars(0);
        else reservationDto.setHostReviewStars(reservation.getHostReviewStars());

        return reservationDto;
    }

    public static Reservation convert(ReservationReq reservationDto) {
        Reservation reservation = new Reservation();
        reservation.setBookedDate(reservationDto.getBookedDate());
        reservation.setLeaveDate(reservationDto.getLeaveDate());
        reservation.setBookedHome(reservationDto.getBookedHomeId());
        reservation.setUserBooked(reservationDto.getUserIdBooked());

        reservation.setHomeReviewDescription(reservationDto.getHomeReviewDescription());
        reservation.setHomeReviewStars(reservationDto.getHomeReviewStars());
        reservation.setHostReviewDescription(reservationDto.getHostReviewDescription());
        reservation.setHostReviewStars(reservationDto.getHostReviewStars());

        log.info("RESERVATION: {}", reservation);
        return reservation;
    }
}