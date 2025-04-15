package com.example.OncativoVende.controllers;

import com.example.OncativoVende.dtos.get.GetLocationDto;
import com.example.OncativoVende.services.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping()
    public ResponseEntity<List<GetLocationDto>> getLocations() {
        List<GetLocationDto> result = locationService.getAllLocations();

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

}
