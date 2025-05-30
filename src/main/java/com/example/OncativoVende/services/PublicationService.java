package com.example.OncativoVende.services;

import com.example.OncativoVende.dtos.get.GetPublicationDto;
import com.example.OncativoVende.dtos.post.PostPublicationDto;
import com.example.OncativoVende.dtos.post.PublicationFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PublicationService {

    GetPublicationDto createPublication(PostPublicationDto postPublicationDto);

    List<GetPublicationDto> getAllPublications();

    GetPublicationDto getPublicationById(Integer id);

    GetPublicationDto updatePublication(Integer id, PostPublicationDto postPublicationDto);

    void deletePublication(Integer id);

    List<GetPublicationDto> getLast10Publications();

    Page<GetPublicationDto> filterPublications(PublicationFilterDto dto);

}
