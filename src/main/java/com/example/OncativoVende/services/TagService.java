package com.example.OncativoVende.services;

import com.example.OncativoVende.dtos.GetTagDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {
    List<GetTagDto> getAllTags();
}
