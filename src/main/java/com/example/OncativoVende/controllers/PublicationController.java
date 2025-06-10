package com.example.OncativoVende.controllers;

import com.example.OncativoVende.dtos.get.GetPublicationDto;
import com.example.OncativoVende.dtos.post.PostPublicationDto;
import com.example.OncativoVende.dtos.post.PublicationFilterDto;
import com.example.OncativoVende.dtos.post.PublicationByUserFilterDto;
import com.example.OncativoVende.dtos.put.PutPublicationDto;
import com.example.OncativoVende.services.PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publications")
@RequiredArgsConstructor
public class PublicationController {

    private final PublicationService publicationService;

    @GetMapping
    public ResponseEntity<List<GetPublicationDto>> getPublications() {
        List<GetPublicationDto> result = publicationService.getAllPublications();

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetPublicationDto> getPublicationById(@PathVariable Integer id) {
        GetPublicationDto result = publicationService.getPublicationById(id);

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping()
    public ResponseEntity<GetPublicationDto> createPublication(@RequestBody PostPublicationDto postPublicationDto) {
        GetPublicationDto result = publicationService.createPublication(postPublicationDto);

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetPublicationDto> updatePublication(@PathVariable Integer id, @RequestBody PutPublicationDto putPublicationDto) {
        GetPublicationDto result = publicationService.updatePublication(id, putPublicationDto);

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublication(@PathVariable Integer id) {
        publicationService.deletePublication(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/last10")
    public ResponseEntity<List<GetPublicationDto>> getLast10Publications() {
        List<GetPublicationDto> result = publicationService.getLast10Publications();

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/filter")
    public Page<GetPublicationDto> filterPublications(@RequestBody PublicationFilterDto filterDto) {
        return publicationService.filterPublications(filterDto);
    }

    @PostMapping("/filter/user/{userId}")
    public Page<GetPublicationDto> filterUserPublications(@PathVariable Integer userId, @RequestBody PublicationByUserFilterDto filterDto) {
        return publicationService.filterUserPublications(userId, filterDto);
    }

    @PostMapping("/add-view/{id}")
    public ResponseEntity<Void> addView(@PathVariable Integer id) {
        publicationService.addView(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reactivate/{id}")
    public ResponseEntity<Void> reactivatePublication(@PathVariable Integer id) {
        publicationService.reactivatePublication(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/is-same-user/{publicationId}/{userId}")
    public ResponseEntity<Boolean> isSameUserPublication(@PathVariable Integer publicationId, @PathVariable Integer userId) {
        boolean isSameUser = publicationService.isSameUserPublication(publicationId, userId);
        return ResponseEntity.ok(isSameUser);
    }

}
