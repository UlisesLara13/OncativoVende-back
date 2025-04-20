package com.example.OncativoVende.services.implementation;

import com.example.OncativoVende.dtos.get.GetEventDto;
import com.example.OncativoVende.dtos.get.GetShortUserDto;
import com.example.OncativoVende.dtos.post.PostEventDto;
import com.example.OncativoVende.entities.EventEntity;
import com.example.OncativoVende.repositores.EventRepository;
import com.example.OncativoVende.repositores.UserRepository;
import com.example.OncativoVende.services.EventService;
import com.example.OncativoVende.services.RatingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final RatingService ratingService;

    @Override
    public GetEventDto createEvent(PostEventDto postEventDto) {
        EventEntity eventEntity = new EventEntity();

        mapPostEventToEntity(postEventDto, eventEntity);
        eventRepository.save(eventEntity);

        GetEventDto getEventDto = new GetEventDto();
        mapEntityToGetEventDto(eventEntity, getEventDto);
        return getEventDto;
    }

    public void mapPostEventToEntity(PostEventDto postEventDto, EventEntity eventEntity) {
        eventEntity.setTitle(postEventDto.getTitle());
        eventEntity.setDescription(postEventDto.getDescription());
        eventEntity.setImage_url(postEventDto.getImage_url());
        eventEntity.setStart_date(postEventDto.getStart_date());
        eventEntity.setEnd_date(postEventDto.getEnd_date());
        eventEntity.setCreated_by_user_id(userRepository.findById(postEventDto.getCreated_by_user_id())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + postEventDto.getCreated_by_user_id())));
    }

    public void mapEntityToGetEventDto(EventEntity eventEntity, GetEventDto getEventDto) {
        getEventDto.setId(eventEntity.getId());
        getEventDto.setTitle(eventEntity.getTitle());
        getEventDto.setDescription(eventEntity.getDescription());
        getEventDto.setImage_url(eventEntity.getImage_url());

        GetShortUserDto getShortUserDto = new GetShortUserDto();
        mapShortUserDto(getShortUserDto, eventEntity);

        getEventDto.setUser(getShortUserDto);
        getEventDto.setStart_date(eventEntity.getStart_date());
        getEventDto.setEnd_date(eventEntity.getEnd_date());
    }

    public void mapShortUserDto(GetShortUserDto getShortUserDto, EventEntity eventEntity) {
        getShortUserDto.setId(eventEntity.getCreated_by_user_id().getId());
        getShortUserDto.setName(eventEntity.getCreated_by_user_id().getName());
        getShortUserDto.setSurname(eventEntity.getCreated_by_user_id().getSurname());
        getShortUserDto.setVerified(eventEntity.getCreated_by_user_id().getVerified());
        getShortUserDto.setAvatar_url(eventEntity.getCreated_by_user_id().getAvatar_url());
        getShortUserDto.setRating(ratingService.calculateRating(eventEntity.getCreated_by_user_id().getId()));
    }

    @Override
    public GetEventDto updateEvent(Integer id, PostEventDto postEventDto) {
        EventEntity eventEntity = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));

        mapPostEventToEntity(postEventDto, eventEntity);
        eventRepository.save(eventEntity);

        GetEventDto getEventDto = new GetEventDto();
        mapEntityToGetEventDto(eventEntity, getEventDto);
        return getEventDto;
    }

    @Override
    public List<GetEventDto> getAllEvents() {
        List<EventEntity> eventEntities = eventRepository.findAll();
        List<GetEventDto> getEventDtos = eventEntities.stream().map(eventEntity -> {
            GetEventDto getEventDto = new GetEventDto();
            mapEntityToGetEventDto(eventEntity, getEventDto);
            return getEventDto;
        }).toList();
        return getEventDtos;
    }
}
