package com.example.OncativoVende.services;

import com.example.OncativoVende.dtos.GetLocationDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocationService {
    List<GetLocationDto> getAllLocations();
}
