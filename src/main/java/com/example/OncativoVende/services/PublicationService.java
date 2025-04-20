package com.example.OncativoVende.services;

import com.example.OncativoVende.dtos.get.GetPublicationDto;
import com.example.OncativoVende.dtos.post.PostPublicationDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PublicationService {

    GetPublicationDto createPublication(PostPublicationDto postPublicationDto);

    List<GetPublicationDto> getAllPublications();

    GetPublicationDto getPublicationById(Integer id);

    GetPublicationDto updatePublication(Integer id, PostPublicationDto postPublicationDto);

    void deletePublication(Integer id);

}
