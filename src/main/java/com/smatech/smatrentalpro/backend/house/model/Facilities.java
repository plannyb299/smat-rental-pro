package com.smatech.smatrentalpro.backend.house.model;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "facilities")
@Getter
@Setter
@Transactional
public class Facilities {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "com.smatech.smatrentalpro.backend.house.config.StringIdGenerator")
    @Column(name = "facilities_id", nullable = false, updatable = false)
    private String facilitiesId;

    private String bathrooms;
    private String bedrooms;
    private boolean ac;
    private boolean tv;
    private boolean parking;
    private boolean kitchen;
    private boolean heating;
    private boolean electricity;
    private boolean wifi;
    private boolean elevator;
    @OneToOne(mappedBy = "facilities")
    private House house;

    @Override
    public String toString() {
        return "Facilities{" +
                "facilitiesId='" + facilitiesId + '\'' +
                ", bathrooms='" + bathrooms + '\'' +
                ", bedrooms='" + bedrooms + '\'' +
                ", ac=" + ac +
                ", tv=" + tv +
                ", parking=" + parking +
                ", kitchen=" + kitchen +
                ", heating=" + heating +
                ", electricity=" + electricity +
                ", wifi=" + wifi +
                ", elevator=" + elevator +
                '}';
    }
}
