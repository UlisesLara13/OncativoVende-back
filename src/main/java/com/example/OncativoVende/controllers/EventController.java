package com.example.OncativoVende.controllers;

import com.example.OncativoVende.dtos.get.GetEventDto;
import com.example.OncativoVende.dtos.get.GetPublicationDto;
import com.example.OncativoVende.dtos.post.PostEventDto;
import com.example.OncativoVende.dtos.post.PostFavoriteDto;
import com.example.OncativoVende.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<GetEventDto> createEvent(@Valid @RequestBody PostEventDto postEventDto) {
        GetEventDto result = eventService.createEvent(postEventDto);

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<GetEventDto>> getAllEvents() {
        List<GetEventDto> result = eventService.getAllEvents();

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetEventDto> updateEvent(@PathVariable Integer id, @Valid @RequestBody PostEventDto postEventDto) {
        GetEventDto result = eventService.updateEvent(id, postEventDto);

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

}
