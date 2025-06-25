package com.example.OncativoVende.services.implementation;

import com.example.OncativoVende.dtos.get.GetRatingDto;
import com.example.OncativoVende.dtos.get.GetShortUserDto;
import com.example.OncativoVende.dtos.post.PostRatingDto;
import com.example.OncativoVende.dtos.put.PutRatingDto;
import com.example.OncativoVende.entities.RatingEntity;
import com.example.OncativoVende.entities.UserEntity;
import com.example.OncativoVende.repositores.PublicationRepository;
import com.example.OncativoVende.repositores.RatingRepository;
import com.example.OncativoVende.repositores.UserRepository;
import com.example.OncativoVende.services.RatingService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
        ratingEntity.setRating(postRatingDto.getRating());
        ratingEntity.setComment(postRatingDto.getComment());
        ratingEntity.setCreatedAt(LocalDate.now());
    }

    public GetShortUserDto mapShortUserEntityToDto(UserEntity userEntity) {
        GetShortUserDto getShortUserDto = new GetShortUserDto();
        getShortUserDto.setId(userEntity.getId());
        getShortUserDto.setName(userEntity.getName());
        getShortUserDto.setSurname(userEntity.getSurname());
        getShortUserDto.setUsername(userEntity.getUsername());
        getShortUserDto.setVerified(userEntity.getVerified());
        getShortUserDto.setRating(this.calculateRating(userEntity.getId()));
        getShortUserDto.setAvatar_url(userEntity.getAvatar_url());
        return getShortUserDto;
    }

    @Override
    public List<GetRatingDto> getRatingsByUser(Integer userId) {
        return ratingRepository.findAllByRatedUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(ratingEntity -> GetRatingDto.builder()
                        .id(ratingEntity.getId())
                        .rating(ratingEntity.getRating())
                        .comment(ratingEntity.getComment())
                        .rater_user(mapShortUserEntityToDto(ratingEntity.getRaterUser()))
                        .rated_user_id(ratingEntity.getRatedUser().getId())
                        .created_at(ratingEntity.getCreatedAt())
                        .build())
                .toList();
    }

    @Override
    public void deleteRating(Integer ratingId) {
        RatingEntity ratingEntity = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Rating not found"));
        ratingRepository.delete(ratingEntity);
    }

    @Override
    public GetRatingDto updateRating(Integer ratingId, PutRatingDto putRatingDto) {
        RatingEntity ratingEntity = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new RuntimeException("Rating not found"));
        ratingEntity.setRating(putRatingDto.getRating());
        ratingEntity.setComment(putRatingDto.getComment());
        ratingRepository.save(ratingEntity);
        return GetRatingDto.builder()
                .id(ratingEntity.getId())
                .rating(ratingEntity.getRating())
                .comment(ratingEntity.getComment())
                .rater_user(mapShortUserEntityToDto(ratingEntity.getRaterUser()))
                .rated_user_id(ratingEntity.getRatedUser().getId())
                .created_at(ratingEntity.getCreatedAt())
                .build();
    }

    @Override
    public GetRatingDto hasRating(Integer ratedUserId, Integer raterUserId) {
        RatingEntity ratingEntity = ratingRepository.findByRatedUserIdAndRaterUserId(ratedUserId, raterUserId);
        if (ratingEntity != null) {
            return GetRatingDto.builder()
                    .id(ratingEntity.getId())
                    .rating(ratingEntity.getRating())
                    .comment(ratingEntity.getComment())
                    .rater_user(mapShortUserEntityToDto(ratingEntity.getRaterUser()))
                    .rated_user_id(ratingEntity.getRatedUser().getId())
                    .created_at(ratingEntity.getCreatedAt())
                    .build();
        }
        return null;
    }

}
