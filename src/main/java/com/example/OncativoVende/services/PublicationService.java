package com.example.OncativoVende.services;

import com.example.OncativoVende.dtos.get.GetPublicationDto;
import com.example.OncativoVende.dtos.post.PostPublicationDto;
import com.example.OncativoVende.dtos.post.PublicationByUserFilterDto;
import com.example.OncativoVende.dtos.post.PublicationFilterDto;
import com.example.OncativoVende.dtos.put.PutPublicationDto;
import com.example.OncativoVende.entities.PublicationEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PublicationService {

    GetPublicationDto createPublication(PostPublicationDto postPublicationDto);

    List<GetPublicationDto> getAllPublications();

    GetPublicationDto getPublicationById(Integer id);

    GetPublicationDto updatePublication(Integer id, PutPublicationDto putPublicationDto);

    void deletePublication(Integer id);

    List<GetPublicationDto> getLast10Publications();

    Page<GetPublicationDto> filterPublications(PublicationFilterDto dto);


    GetPublicationDto getPublicationActiveById(Integer id);

    Page<GetPublicationDto> filterUserPublications(Integer userId, PublicationByUserFilterDto dto);

    void addView(Integer id);

    void reactivatePublication(Integer id);

    boolean isSameUserPublication(Integer publicationId, Integer userId);

    void deleteAllPublicationsByUserId(Integer userId);

    void mapPublicationEntityToDto(PublicationEntity publicationEntity, GetPublicationDto getPublicationDto);

    void deleteAllPublicationsByUserPermanently(Integer userId);

}
