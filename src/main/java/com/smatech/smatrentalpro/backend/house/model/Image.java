package com.smatech.smatrentalpro.backend.house.model;


import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "images")
@Data
@Transactional
public class Image{

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "com.smatech.smatrentalpro.backend.house.config.StringIdGenerator")
    @Column(name = "image_id", nullable = false, updatable = false)
    private String imageId;

    @Column(name = "imageUrl")
    private String imageUrl;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private House house;
}
