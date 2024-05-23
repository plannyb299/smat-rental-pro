package com.smatech.smatrentalpro.backend.house.model;


import com.smatech.smatrentalpro.backend.commons.AbstractEntity;
import com.smatech.smatrentalpro.backend.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "house")
@Getter
@Setter
public class House extends AbstractEntity {


    @Version
    private Long version;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(name = "street")
    private String street;

    @Column(name = "address")
    private String address;

    @Column(name = "rooms")
    private Integer rooms;

    @Column(name = "bedrooms")
    private Integer bedrooms;

    @Column(name = "bathrooms")
    private Integer bathrooms;

    @Column(name = "shortAddress")
    private String shortAddress;

    @Column(name = "image")
    private String image;

    @Column(name = "price")
    private Double price;

    @Column(name = "rent")
    private Double rent;

    @Column(name = "description")
    private String description;

    @Column(name = "neighbourhood")
    private String neighbourhood;

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL)
    private List<Image> images;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "facilities_id")
    private Facilities facilities;

    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    private Category category;

    @OneToMany(mappedBy = "bookedHome", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Reservation> reservations;


    @Override
    public String toString() {
        return "House{" +
                "version=" + version +
                ", owner=" + owner +
                ", street='" + street + '\'' +
                ", address='" + address + '\'' +
                ", rooms=" + rooms +
                ", bedrooms=" + bedrooms +
                ", bathrooms=" + bathrooms +
                ", shortAddress='" + shortAddress + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", rent=" + rent +
                ", description='" + description + '\'' +
                ", neighbourhood='" + neighbourhood + '\'' +
                ", facilities=" + (facilities != null ? facilities.getFacilitiesId() : null) +
                ", category=" + category +
                ", reservations=" + reservations +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof House house)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(version, house.version) && Objects.equals(owner, house.owner) && Objects.equals(street, house.street) && Objects.equals(address, house.address) && Objects.equals(rooms, house.rooms) && Objects.equals(bedrooms, house.bedrooms) && Objects.equals(bathrooms, house.bathrooms) && Objects.equals(shortAddress, house.shortAddress) && Objects.equals(image, house.image) && Objects.equals(price, house.price) && Objects.equals(rent, house.rent) && Objects.equals(description, house.description) && Objects.equals(neighbourhood, house.neighbourhood) && Objects.equals(location, house.location) && Objects.equals(facilities, house.facilities) && Objects.equals(category, house.category) && Objects.equals(reservations, house.reservations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), version, owner, street, address, rooms, bedrooms, bathrooms, shortAddress, image, price, rent, description, neighbourhood, location, facilities, category, reservations);
    }
}
