package com.example.OncativoVende.services;

import com.example.OncativoVende.dtos.get.GetRatingDto;
import com.example.OncativoVende.dtos.post.PostRatingDto;
import com.example.OncativoVende.dtos.put.PutRatingDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface RatingService {
    BigDecimal calculateRating(Integer userId);

    boolean addRating(PostRatingDto postRatingDto);

    List<GetRatingDto> getRatingsByUser(Integer userId);

    void deleteRating(Integer ratingId);

    GetRatingDto updateRating(Integer ratingId, PutRatingDto putRatingDto);

    GetRatingDto hasRating(Integer ratedUserId, Integer raterUserId);

}
