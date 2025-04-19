package com.example.OncativoVende.services;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface RatingService {
    BigDecimal calculateRating(Integer userId);
}
