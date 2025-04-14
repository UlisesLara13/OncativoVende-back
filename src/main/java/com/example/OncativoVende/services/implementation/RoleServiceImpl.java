package com.example.OncativoVende.services.implementation;

import com.example.OncativoVende.dtos.GetRoleDto;
import com.example.OncativoVende.repositores.RoleRepository;
import com.example.OncativoVende.services.RoleService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<GetRoleDto> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleEntity -> new GetRoleDto(roleEntity.getId(), roleEntity.getDescription()))
                .toList();
    }
}
