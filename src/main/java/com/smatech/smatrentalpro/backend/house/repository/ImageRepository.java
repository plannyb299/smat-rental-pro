package com.smatech.smatrentalpro.backend.house.repository;

import com.smatech.smatrentalpro.backend.house.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
