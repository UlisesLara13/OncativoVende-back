package com.example.OncativoVende.services.implementation;

import com.example.OncativoVende.dtos.get.GetPublicationDto;
import com.example.OncativoVende.dtos.post.PostFavoriteDto;
import com.example.OncativoVende.entities.FavoriteEntity;
import com.example.OncativoVende.repositores.FavoriteRepository;
import com.example.OncativoVende.repositores.PublicationRepository;
import com.example.OncativoVende.repositores.UserRepository;
import com.example.OncativoVende.services.FavoriteService;
import com.example.OncativoVende.services.PublicationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@RequiredArgsConstructor
@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;

    private final PublicationService publicationService;

    private final PublicationRepository publicationRepository;

    private final UserRepository userRepository;

    @Override
    public GetPublicationDto createFavorite(PostFavoriteDto postFavoriteDto) {
        FavoriteEntity favoriteEntity = new FavoriteEntity();
        mapPostFavoriteToEntity(postFavoriteDto, favoriteEntity);
        favoriteRepository.save(favoriteEntity);

        return publicationService.getPublicationById(postFavoriteDto.getPublication_id());
    }

    public void mapPostFavoriteToEntity(PostFavoriteDto postFavoriteDto, FavoriteEntity favoriteEntity) {
        favoriteEntity.setPublication(publicationRepository.findById(postFavoriteDto.getPublication_id())
                .orElseThrow(() -> new EntityNotFoundException("Publication not found with id: " + postFavoriteDto.getPublication_id())));
        favoriteEntity.setUser(userRepository.findById(postFavoriteDto.getUser_id())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + postFavoriteDto.getUser_id())));
    }

    @Override
    public boolean isFavorite(Integer publicationId, Integer userId) {
        return favoriteRepository.existsByPublicationIdAndUserId(publicationId, userId);
    }

    @Override
    public void deleteFavorite(Integer publicationId, Integer userId) {
        FavoriteEntity favoriteEntity = favoriteRepository.findByPublicationIdAndUserId(publicationId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Favorite not found with publication id: " + publicationId + " and user id: " + userId));
        favoriteRepository.delete(favoriteEntity);
    }



}
