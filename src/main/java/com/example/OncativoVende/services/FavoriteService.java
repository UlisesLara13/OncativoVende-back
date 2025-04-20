package com.example.OncativoVende.services;

import com.example.OncativoVende.dtos.get.GetPublicationDto;
import com.example.OncativoVende.dtos.post.PostFavoriteDto;
import org.springframework.stereotype.Service;

@Service
public interface FavoriteService {

    GetPublicationDto createFavorite(PostFavoriteDto postFavoriteDto);

}
