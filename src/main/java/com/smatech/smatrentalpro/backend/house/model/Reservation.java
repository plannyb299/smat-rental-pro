package com.smatech.smatrentalpro.backend.house.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smatech.smatrentalpro.backend.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="reservation")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Reservation  {


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "com.smatech.smatrentalpro.backend.house.config.StringIdGenerator")
    @Column(name = "reservation_id", nullable = false, updatable = false)
    private String reservationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="id", nullable = false)
    private House bookedHome;

    @Column(name = "booked_date", nullable = false)
    private LocalDate bookedDate;

    @Column(name = "leave_date", nullable = false)
    private LocalDate leaveDate;

    @Column(name = "booked")
    private int booked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id", nullable = false)
    private User userBooked;

    @Column(name = "host_review_stars")
    private Integer hostReviewStars;

    @Column(name = "host_review_description")
    private String hostReviewDescription;

    @Column(name = "home_review_stars")
    private Integer homeReviewStars;

    @Column(name = "home_review_description")
    private String homeReviewDescription;
}
