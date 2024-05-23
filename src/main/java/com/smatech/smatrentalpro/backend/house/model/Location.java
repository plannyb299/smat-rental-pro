package com.smatech.smatrentalpro.backend.house.model;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
@Table(name = "location")
@Transactional
public class Location {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "com.smatech.smatrentalpro.backend.house.config.StringIdGenerator")
    @Column(name = "location_id", nullable = false, updatable = false)
    private String locationId;

    @Column(name = "address")
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @OneToOne(mappedBy = "location")
    private House house;

    @Override
    public String toString() {
        return "Location{" +
                "locationId='" + locationId + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
