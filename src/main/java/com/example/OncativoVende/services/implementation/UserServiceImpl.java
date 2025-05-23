package com.example.OncativoVende.services.implementation;

import com.example.OncativoVende.dtos.get.GetUserDto;
import com.example.OncativoVende.dtos.post.ChangePassword;
import com.example.OncativoVende.dtos.post.PostLoginDto;
import com.example.OncativoVende.dtos.post.PostUserDto;
import com.example.OncativoVende.dtos.put.PutUserDto;
import com.example.OncativoVende.entities.*;
import com.example.OncativoVende.repositores.*;
import com.example.OncativoVende.security.PasswordUtil;
import com.example.OncativoVende.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
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

    private final RatingServiceImpl ratingService;

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public List<GetUserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userEntity -> {
                    GetUserDto getUserDto = new GetUserDto();
                    mapUserEntityToDto(userEntity, getUserDto);
                    return getUserDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public GetUserDto getUserById(Integer id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        GetUserDto getUserDto = new GetUserDto();
        mapUserEntityToDto(userEntity, getUserDto);
        return getUserDto;
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
        getUserDto.setRating(ratingService.calculateRating(userEntity.getId()));
        getUserDto.setSubscription(getSubscription(userEntity));
        mapRolesToGetUserDto(getUserDto);

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

    public void mapUserDtoToEntity(PutUserDto putUserDto, UserEntity userEntity) {
        userEntity.setName(putUserDto.getName());
        userEntity.setSurname(putUserDto.getSurname());
        userEntity.setUsername(putUserDto.getUsername());
        userEntity.setAvatar_url(putUserDto.getAvatar_url());
        userEntity.setEmail(putUserDto.getEmail());
        userEntity.setLocation_id(getLocationById(putUserDto.getLocation_id()));
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

    public String getSubscription(UserEntity userEntity) {
        LocalDate currentDate = LocalDate.now();
        SubscriptionEntity subscriptionEntity = subscriptionRepository.findByUserIdAndEndDateAfter(userEntity, currentDate);

        if (subscriptionEntity != null) {
            assignPremiumRoleToUser(userEntity);
            return subscriptionEntity.getSubscription_type_id().getDescription();

        } else {
            deletePremiumRoleFromUser(userEntity);
            return "NO";
        }
    }

    public void assignPremiumRoleToUser(UserEntity userEntity) {
        RoleEntity roleEntity = roleRepository.findByDescription("PREMIUM");
        if (roleEntity == null) {
            throw new EntityNotFoundException("Role not found with description: PREMIUM");
        }

        boolean alreadyAssigned = userRoleRepository.existsByUserAndRole(userEntity, roleEntity);
        if (alreadyAssigned) {
            return;
        }
        
        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setUser(userEntity);
        userRoleEntity.setRole(roleEntity);
        userRoleRepository.save(userRoleEntity);
    }

    public void deletePremiumRoleFromUser(UserEntity userEntity) {
        RoleEntity roleEntity = roleRepository.findByDescription("PREMIUM");
        if (roleEntity == null) {
            throw new EntityNotFoundException("Role not found with description: PREMIUM");
        }
        List<UserRoleEntity> userRoles = userRoleRepository.findAllByUser_Id(userEntity.getId());
        for (UserRoleEntity userRole : userRoles) {
            if (userRole.getRole().getId().equals(roleEntity.getId())) {
                userRoleRepository.delete(userRole);
            }
        }
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

    @Override
    public void deleteUser(Integer id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        userEntity.setActive(false);
        userRepository.save(userEntity);
    }

    @Override
    public void activeUser(Integer id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        userEntity.setActive(true);
        userRepository.save(userEntity);
    }

    @Override
    public void verifyUser(Integer id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        userEntity.setVerified(true);
        userRepository.save(userEntity);
    }

    @Override
    public void unverifyUser(Integer id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        userEntity.setVerified(false);
        userRepository.save(userEntity);
    }

    @Override
    public GetUserDto getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
        GetUserDto getUserDto = new GetUserDto();
        mapUserEntityToDto(userEntity, getUserDto);
        return getUserDto;
    }

    @Override
    public GetUserDto updateUser(PutUserDto putUserDto, Integer id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        validateUserUpdate(putUserDto, id);
        mapUserDtoToEntity(putUserDto, userEntity);
        updateUserRoles(userEntity, putUserDto.getRoles());
        userRepository.save(userEntity);


        GetUserDto getUserDto = new GetUserDto();
        mapUserEntityToDto(userEntity, getUserDto);
        return getUserDto;

    }

    public void validateUserUpdate(PutUserDto putUserDto, Integer userId) {
        validateUsernameUpdate(putUserDto.getUsername(), userId);
        validateEmailUpdate(putUserDto.getEmail(), userId);
    }

    public void validateUsernameUpdate(String username, Integer userId) {
        userRepository.findByUsername(username)
                .ifPresent(user -> {
                    if (!user.getId().equals(userId)) {
                        throw new IllegalArgumentException("Error updating user: username already in use by another user.");
                    }
                });
    }

    public void validateEmailUpdate(String email, Integer userId) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    if (!user.getId().equals(userId)) {
                        throw new IllegalArgumentException("Error updating user: email already in use by another user.");
                    }
                });
    }

    public void updateUserRoles(UserEntity userEntity, Integer[] roles) {
        List<UserRoleEntity> userRoles = userRoleRepository.findAllByUser_Id(userEntity.getId());
        for (UserRoleEntity userRole : userRoles) {
            userRoleRepository.delete(userRole);
        }
        assignRolesToUser(userEntity, roles);
    }

    @Override
    public GetUserDto verifyLogin(PostLoginDto postLoginDto) {
        GetUserDto user = this.getUserByUsername(postLoginDto.getEmail());

        if (user == null) {
            user = this.getUserByEmail(postLoginDto.getEmail());
        }

        if (user != null && passwordEncoder.checkPassword(postLoginDto.getPassword(), user.getPassword())) {
            return user;
        }

        return null;
    }

    public GetUserDto getUserByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username).orElse(null);

        if (userEntity == null) {
            return null;
        }
        return getUserById(userEntity.getId());
    }

    @Override
    @Transactional
    public void changePassword(ChangePassword changePasswordDto) {
        GetUserDto getUserDto = this.getUserByEmail(changePasswordDto.getEmail());
        if (getUserDto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with email: " + changePasswordDto.getEmail());
        }
        UserEntity user = userRepository.findById(getUserDto.getId())
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User not found with email: " + changePasswordDto.getEmail()));


        if (!PasswordUtil.checkPassword(changePasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(401), "Current password is incorrect.");
        }

        String hashedNewPassword = PasswordUtil.hashPassword(changePasswordDto.getNewPassword());
        user.setPassword(hashedNewPassword);

        userRepository.save(user);
    }

    @Override
    public boolean updateAvatarUrl(String avatarUrl, Integer id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        userEntity.setAvatar_url(avatarUrl);
        userRepository.save(userEntity);
        return true;
    }

}
