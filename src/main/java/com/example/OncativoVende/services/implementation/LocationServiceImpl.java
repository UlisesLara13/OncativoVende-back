package com.example.OncativoVende.services.implementation;

import com.example.OncativoVende.dtos.get.GetLocationDto;
import com.example.OncativoVende.repositores.LocationRepository;
import com.example.OncativoVende.services.LocationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@RequiredArgsConstructor
@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Override
    public List<GetLocationDto> getAllLocations() {
        return locationRepository.findAll()
                .stream()
                .map(locationEntity -> new GetLocationDto(locationEntity.getId(), locationEntity.getDescription()))
                .toList();
    }
}
