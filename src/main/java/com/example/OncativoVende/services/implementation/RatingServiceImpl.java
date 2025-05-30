package com.example.OncativoVende.services.implementation;

import com.example.OncativoVende.dtos.post.PostRatingDto;
import com.example.OncativoVende.entities.RatingEntity;
import com.example.OncativoVende.repositores.PublicationRepository;
import com.example.OncativoVende.repositores.RatingRepository;
import com.example.OncativoVende.repositores.UserRepository;
import com.example.OncativoVende.services.RatingService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    private final UserRepository userRepository;

    private final PublicationRepository publicationRepository;

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

    @Override
    public boolean addRating(PostRatingDto postRatingDto) {
        if (postRatingDto == null) {
            return false;
        } else {
            RatingEntity ratingEntity = new RatingEntity();
            mapToRatingEntity(postRatingDto, ratingEntity);
            ratingRepository.save(ratingEntity);
            return true;
        }
    }

    public void mapToRatingEntity(PostRatingDto postRatingDto, RatingEntity ratingEntity) {
        ratingEntity.setRaterUser(userRepository.findById(postRatingDto.getRater_user_id())
                .orElseThrow(() -> new RuntimeException("User not found")));
        ratingEntity.setRatedUser(userRepository.findById(postRatingDto.getRated_user_id())
                .orElseThrow(() -> new RuntimeException("User not found")));
        ratingEntity.setPublication(publicationRepository.findById(postRatingDto.getPublication_id())
                .orElseThrow(() -> new RuntimeException("Publication not found")));
        ratingEntity.setRating(postRatingDto.getRating());
        ratingEntity.setComment(postRatingDto.getComment());
        ratingEntity.setCreated_at(LocalDate.now());
    }
}
