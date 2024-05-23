package com.smatech.smatrentalpro.backend.house.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationRes {

    private String reservationId;

    private String bookedHomeId;

    private LocalDate bookedDate;

    private LocalDate leaveDate;

    int isBooked;

    private Integer userIdBooked;
    private String userNameBooked;

    private Integer hostReviewStars;

    private String hostReviewDescription;

    private Integer homeReviewStars;

    private String homeReviewDescription;
}
