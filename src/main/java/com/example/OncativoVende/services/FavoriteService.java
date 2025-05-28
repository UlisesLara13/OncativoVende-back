package com.example.OncativoVende.services;

import com.example.OncativoVende.dtos.get.GetPublicationDto;
import com.example.OncativoVende.dtos.post.PostFavoriteDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FavoriteService {

    GetPublicationDto createFavorite(PostFavoriteDto postFavoriteDto);

    boolean isFavorite(Integer publicationId, Integer userId);

    void deleteFavorite(Integer publicationId, Integer userId);

    List<GetPublicationDto> getFavoritesByUserId(Integer userId);

}
