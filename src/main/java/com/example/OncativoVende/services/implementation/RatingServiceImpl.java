package com.example.OncativoVende.services.implementation;

import com.example.OncativoVende.repositores.RatingRepository;
import com.example.OncativoVende.services.RatingService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    @Override
    public BigDecimal calculateRating(Integer userId) {
        long count = ratingRepository.countByRatedUserId(userId);

        if (count == 0) {
            return BigDecimal.ZERO.setScale(1);
        }

        return ratingRepository.findAllByRatedUserId(userId)
                .stream()
                .map(ratingEntity -> ratingEntity.getRating())
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(count), 1, BigDecimal.ROUND_HALF_UP);
    }
}
