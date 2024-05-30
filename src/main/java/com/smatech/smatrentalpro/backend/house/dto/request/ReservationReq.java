package com.smatech.smatrentalpro.backend.house.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationReq {

    private String bookedHomeId;

    private LocalDate bookedDate;

    private LocalDate leaveDate;


    private Integer userIdBooked;
    private String userNameBooked;

    int hostReviewStars;

    String hostReviewDescription;

    int homeReviewStars;

    String homeReviewDescription;
}
