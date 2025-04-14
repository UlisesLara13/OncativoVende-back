package com.example.OncativoVende.services.implementation;

import com.example.OncativoVende.dtos.GetCategoryDto;
import com.example.OncativoVende.repositores.CategoryRepository;
import com.example.OncativoVende.services.CategoryService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<GetCategoryDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryEntity -> new GetCategoryDto(categoryEntity.getId(), categoryEntity.getDescription()))
                .toList();
    }
}
