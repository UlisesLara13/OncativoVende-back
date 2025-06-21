package com.example.OncativoVende.services;

import com.example.OncativoVende.dtos.get.GetEventDto;
import com.example.OncativoVende.dtos.post.PostEventDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {

    GetEventDto createEvent(PostEventDto postEventDto);

    GetEventDto updateEvent(Integer id, PostEventDto postEventDto);

    List<GetEventDto> getAllEvents();

    GetEventDto getLastEvent();

    boolean finalizeEvent(Integer id);


}
