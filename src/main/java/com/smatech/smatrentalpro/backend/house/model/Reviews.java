package com.smatech.smatrentalpro.backend.house.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Reviews {
    long totalReviews;
    double average;
    List<Integer> reviews;
}
