package com.example.OncativoVende.controllers;

import com.example.OncativoVende.dtos.get.GetPublicationDto;
import com.example.OncativoVende.dtos.get.GetUserDto;
import com.example.OncativoVende.dtos.post.PostFavoriteDto;
import com.example.OncativoVende.dtos.post.PostUserDto;
import com.example.OncativoVende.services.FavoriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<GetPublicationDto> createFavorite(@Valid @RequestBody PostFavoriteDto postFavoriteDto) {
        GetPublicationDto result = favoriteService.createFavorite(postFavoriteDto);

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/is-favorite")
    public ResponseEntity<Boolean> isFavorite(@Valid @RequestBody PostFavoriteDto postFavoriteDto) {
        boolean result = favoriteService.isFavorite(postFavoriteDto.getPublication_id(), postFavoriteDto.getUser_id());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteFavorite(@Valid @RequestBody PostFavoriteDto postFavoriteDto) {
        favoriteService.deleteFavorite(postFavoriteDto.getPublication_id(), postFavoriteDto.getUser_id());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GetPublicationDto>> getFavoritesByUserId(@PathVariable Integer userId) {
        List<GetPublicationDto> favorites = favoriteService.getFavoritesByUserId(userId);
        if (favorites == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(favorites);
    }
}
