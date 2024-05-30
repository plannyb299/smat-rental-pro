package com.smatech.smatrentalpro.backend.house.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smatech.smatrentalpro.backend.commons.AbstractEntity;
import com.smatech.smatrentalpro.backend.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="reservation")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Reservation {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "com.smatech.smatrentalpro.backend.house.config.StringIdGenerator")
    @Column(name = "reservation_id", nullable = false, updatable = false)
    private String reservationId;


    @Column(name ="bookedHome")
    private String bookedHome;

    @Column(name = "booked_date")
    private LocalDate bookedDate;

    @Column(name = "leave_date")
    private LocalDate leaveDate;

    @Column(name = "booked")
    private int booked;


    @Column(name ="userBooked")
    private Integer userBooked;

    @Column(name = "host_review_stars")
    private Integer hostReviewStars;

    @Column(name = "host_review_description")
    private String hostReviewDescription;

    @Column(name = "home_review_stars")
    private Integer homeReviewStars;

    @Column(name = "home_review_description")
    private String homeReviewDescription;


    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId='" + reservationId + '\'' +
                ", bookedHome='" + bookedHome + '\'' +
                ", bookedDate=" + bookedDate +
                ", booked=" + booked +
                ", userBooked='" + userBooked + '\'' +
                ", hostReviewStars=" + hostReviewStars +
                ", hostReviewDescription='" + hostReviewDescription + '\'' +
                ", homeReviewStars=" + homeReviewStars +
                ", homeReviewDescription='" + homeReviewDescription + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;
        Reservation that = (Reservation) o;
        return booked == that.booked &&
                Objects.equals(reservationId, that.reservationId) &&
                Objects.equals(bookedHome, that.bookedHome) &&
                Objects.equals(bookedDate, that.bookedDate) &&
                Objects.equals(userBooked, that.userBooked) &&
                Objects.equals(hostReviewStars, that.hostReviewStars) &&
                Objects.equals(hostReviewDescription, that.hostReviewDescription) &&
                Objects.equals(homeReviewStars, that.homeReviewStars) &&
                Objects.equals(homeReviewDescription, that.homeReviewDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId, bookedHome, bookedDate, booked, userBooked, hostReviewStars, hostReviewDescription, homeReviewStars, homeReviewDescription);
    }
}
