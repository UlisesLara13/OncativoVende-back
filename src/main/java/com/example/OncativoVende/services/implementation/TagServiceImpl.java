package com.example.OncativoVende.services.implementation;

import com.example.OncativoVende.dtos.get.GetTagDto;
import com.example.OncativoVende.repositores.TagRepository;
import com.example.OncativoVende.services.TagService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@RequiredArgsConstructor
@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<GetTagDto> getAllTags() {
        return tagRepository.findAll()
                .stream()
                .map(tagEntity -> new GetTagDto(tagEntity.getId(), tagEntity.getDescription()))
                .toList();
    }
}
