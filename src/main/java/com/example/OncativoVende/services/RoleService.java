package com.example.OncativoVende.services;

import com.example.OncativoVende.dtos.get.GetRoleDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {
    List<GetRoleDto> getAllRoles();
}
