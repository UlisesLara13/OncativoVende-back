package com.example.OncativoVende.services;

import com.example.OncativoVende.dtos.post.PostRatingDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface RatingService {
    BigDecimal calculateRating(Integer userId);

    boolean addRating(PostRatingDto postRatingDto);

}
