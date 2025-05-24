package com.example.OncativoVende.controllers;

import com.example.OncativoVende.dtos.get.GetRatingDto;
import com.example.OncativoVende.dtos.post.PostRatingDto;
import com.example.OncativoVende.dtos.put.PutRatingDto;
import com.example.OncativoVende.services.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<Boolean> addRating(@Valid @RequestBody PostRatingDto postRatingDto) {
        Boolean result = ratingService.addRating(postRatingDto);

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GetRatingDto>> getRatingsByUser(@PathVariable Integer userId) {
        List<GetRatingDto> ratings = ratingService.getRatingsByUser(userId);
        return ResponseEntity.ok(ratings);
    }

    @DeleteMapping("/{ratingId}")
    public ResponseEntity<Void> deleteRating(@PathVariable Integer ratingId) {
        ratingService.deleteRating(ratingId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{ratingId}")
    public ResponseEntity<GetRatingDto> updateRating(
            @PathVariable Integer ratingId,
            @Valid @RequestBody PutRatingDto putRatingDto) {
        GetRatingDto updated = ratingService.updateRating(ratingId, putRatingDto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/hasrating/{ratedUserId}/{raterUserId}")
    public ResponseEntity<GetRatingDto> hasRating(@PathVariable Integer ratedUserId, @PathVariable Integer raterUserId) {
        GetRatingDto hasRating = ratingService.hasRating(ratedUserId, raterUserId);
        if (hasRating == null) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(hasRating);
    }



}
