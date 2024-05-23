package com.smatech.smatrentalpro.backend.house.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "residencies")
@Data
public class Residency {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String description;
    private Double price;
    private String address;
    private String country;
    private String city;
    private String image;
    @OneToOne
    @JoinColumn(name = "id")
    private Facilities facilities;


}
