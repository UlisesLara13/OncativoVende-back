package com.example.OncativoVende.services.implementation;

import com.example.OncativoVende.dtos.get.GetUserDto;
import com.example.OncativoVende.dtos.post.PostUserDto;
import com.example.OncativoVende.entities.LocationEntity;
import com.example.OncativoVende.entities.RoleEntity;
import com.example.OncativoVende.entities.UserEntity;
import com.example.OncativoVende.entities.UserRoleEntity;
import com.example.OncativoVende.repositores.LocationRepository;
import com.example.OncativoVende.repositores.RoleRepository;
import com.example.OncativoVende.repositores.UserRepository;
import com.example.OncativoVende.repositores.UserRoleRepository;
import com.example.OncativoVende.security.PasswordUtil;
import com.example.OncativoVende.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final LocationRepository locationRepository;

    private final UserRoleRepository userRoleRepository;

    private final RoleRepository roleRepository;

    private final PasswordUtil passwordEncoder;

    @Override
    public List<GetUserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userEntity -> {
                    GetUserDto getUserDto = new GetUserDto();
                    mapUserEntityToDto(userEntity, getUserDto);
                    mapRolesToGetUserDto(getUserDto);
                    return getUserDto;
                })
                .collect(Collectors.toList());
    }

    public void mapUserEntityToDto(UserEntity userEntity, GetUserDto getUserDto) {
        getUserDto.setId(userEntity.getId());
        getUserDto.setName(userEntity.getName());
        getUserDto.setSurname(userEntity.getSurname());
        getUserDto.setActive(userEntity.getActive());
        getUserDto.setUsername(userEntity.getUsername());
        getUserDto.setPassword(userEntity.getPassword());
        getUserDto.setAvatar_url(userEntity.getAvatar_url());
        getUserDto.setEmail(userEntity.getEmail());
        getUserDto.setVerified(userEntity.getVerified());
        getUserDto.setLocation(userEntity.getLocation_id().getDescription());

    }

    public void mapRolesToGetUserDto(GetUserDto getUserDto) {
        List<UserRoleEntity> userRoles = userRoleRepository.findAllByUser_Id(getUserDto.getId());

        String[] roleNames = userRoles.stream()
                .map(userRole -> userRole.getRole().getDescription())
                .toArray(String[]::new);

        getUserDto.setRoles(roleNames);
    }

    public LocationEntity getLocationById(Integer locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));
    }

    public void mapUserDtoToEntity(PostUserDto postUserDto, UserEntity userEntity) {
        userEntity.setName(postUserDto.getName());
        userEntity.setSurname(postUserDto.getSurname());
        userEntity.setUsername(postUserDto.getUsername());
        userEntity.setPassword(postUserDto.getPassword());
        userEntity.setAvatar_url(postUserDto.getAvatar_url());
        userEntity.setEmail(postUserDto.getEmail());
        userEntity.setLocation_id(getLocationById(postUserDto.getLocation_id()));
        userEntity.setActive(true);
        userEntity.setVerified(false);
    }

    @Override
    public GetUserDto createUser(PostUserDto postUserDto) {
        validateUser(postUserDto);
        String hashedPassword = passwordEncoder.hashPassword(postUserDto.getPassword());
        postUserDto.setPassword(hashedPassword);
        UserEntity userEntity = new UserEntity();
        mapUserDtoToEntity(postUserDto, userEntity);

        UserEntity savedUser = userRepository.save(userEntity);
        assignRolesToUser(savedUser, postUserDto.getRoles());

        GetUserDto getUserDto = new GetUserDto();
        mapUserEntityToDto(savedUser, getUserDto);

        return getUserDto;
    }

    public void validateUsername(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Error creating user: username already in use.");
        }
    }

    public void validateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Error creating user: email already in use.");
        }
    }

    public void validateUser(PostUserDto postUserDto) {
        validateEmail(postUserDto.getEmail());
        validateUsername(postUserDto.getUsername());
    }

    public void saveUserRole(UserEntity userSaved, RoleEntity roleEntity) {
        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setUser(userSaved);
        userRoleEntity.setRole(roleEntity);
        userRoleRepository.save(userRoleEntity);
    }

    public void assignRolesToUser(UserEntity userSaved, Integer[] roleIds) {
        for (Integer roleId : roleIds) {
            RoleEntity roleEntity = roleRepository.findById(roleId)
                    .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + roleId));
            if (roleEntity != null) {
                saveUserRole(userSaved, roleEntity);
            }
        }
    }

}
