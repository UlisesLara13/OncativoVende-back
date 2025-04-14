package com.example.OncativoVende.services;

import com.example.OncativoVende.dtos.GetCategoryDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<GetCategoryDto> getAllCategories();
}
