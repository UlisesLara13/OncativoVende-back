package com.example.OncativoVende.services.implementation;

import com.example.OncativoVende.dtos.get.GetPublicationDto;
import com.example.OncativoVende.dtos.get.GetUserDto;
import com.example.OncativoVende.dtos.post.*;
import com.example.OncativoVende.dtos.put.PutPersonalDataDto;
import com.example.OncativoVende.dtos.put.PutUserDto;
import com.example.OncativoVende.entities.*;
import com.example.OncativoVende.repositores.*;
import com.example.OncativoVende.security.PasswordUtil;
import com.example.OncativoVende.services.PublicationService;
import com.example.OncativoVende.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;
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

    private final JavaMailSender mailSender;
    private final PublicationService publicationService;

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

    @Override
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
        getUserDto.setCreated_at(userEntity.getCreated_at());
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
        userEntity.setCreated_at(LocalDate.now());
        userEntity.setActive(true);
        userEntity.setVerified(false);
    }

    public void mapUserDtoToEntity(PutUserDto putUserDto, UserEntity userEntity) {
        userEntity.setName(putUserDto.getName());
        userEntity.setSurname(putUserDto.getSurname());
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
        publicationService.deleteAllPublicationsByUserId(userEntity.getId());
        userEntity.setActive(false);
        userRepository.save(userEntity);
    }

    @Transactional
    @Override
    public void deleteUserPermanently(Integer id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        publicationService.deleteAllPublicationsByUserPermanently(userEntity.getId());
        userRoleRepository.deleteAllByUser_Id(userEntity.getId());
        userRepository.delete(userEntity);
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

    @Override
    public GetUserDto updatePersonalData(PutPersonalDataDto putPersonalDataDto, Integer id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        validateEmailUpdate(putPersonalDataDto.getEmail(), id);
        userEntity.setName(putPersonalDataDto.getName());
        if (putPersonalDataDto.getSurname() != null) {
            userEntity.setSurname(putPersonalDataDto.getSurname());
        }
        else {
            userEntity.setSurname("");
        }
        userEntity.setEmail(putPersonalDataDto.getEmail());
        userEntity.setLocation_id(getLocationById(putPersonalDataDto.getLocation_id()));

        userRepository.save(userEntity);

        GetUserDto getUserDto = new GetUserDto();
        mapUserEntityToDto(userEntity, getUserDto);
        return getUserDto;
    }

    public void validateUserUpdate(PutUserDto putUserDto, Integer userId) {
        validateEmailUpdate(putUserDto.getEmail(), userId);
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

    @Override
    public void sendRecoveryCode(RecoveryRequestDto request) {
        UserEntity user = findByEmailOrUsername(request.getEmailOrUsername());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado.");
        }

        String code = String.format("%06d", new Random().nextInt(999999));
        user.setRecovery_code(code);
        user.setRecovery_code_expiration(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        sendRecoveryCode(user.getEmail(), code);
    }

    @Override
    public void resetPassword(ResetPasswordDto dto) {
        UserEntity user = findByEmailOrUsername(dto.getEmailOrUsername());
        if (user == null || !dto.getCode().equals(user.getRecovery_code())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código inválido.");
        }

        if (user.getRecovery_code_expiration().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código expirado.");
        }

        user.setPassword(passwordEncoder.hashPassword(dto.getNewPassword()));
        user.setRecovery_code(null);
        user.setRecovery_code_expiration(null);
        userRepository.save(user);
    }

    public UserEntity findByEmailOrUsername(String emailOrUsername) {
        return userRepository.findByEmail(emailOrUsername)
                .or(() -> userRepository.findByUsername(emailOrUsername))
                .orElse(null);
    }

    public void sendRecoveryCode(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Recuperación de contraseña");
        message.setText("Tu código de recuperación es: " + code);
        mailSender.send(message);
    }

    @Override
    public Page<GetUserDto> filterUsers(UserFilterDto dto) {
        String searchTerm = (dto.getSearchTerm() != null && !dto.getSearchTerm().isBlank())
                ? dto.getSearchTerm().toLowerCase()
                : null;

        List<String> roles = (dto.getRoles() != null && !dto.getRoles().isEmpty())
                ? dto.getRoles().stream().map(String::toLowerCase).toList()
                : null;

        String location = (dto.getLocation() != null && !dto.getLocation().isBlank())
                ? dto.getLocation().toLowerCase()
                : null;

        Boolean active = dto.getActive();
        Boolean verified = dto.getVerified();

        String sortBy = (dto.getSortBy() != null && isValidSortField(dto.getSortBy()))
                ? dto.getSortBy()
                : "surname";

        String sortDir = (dto.getSortDir() != null && dto.getSortDir().equalsIgnoreCase("ASC"))
                ? "ASC"
                : "DESC";

        int page = (dto.getPage() >= 0) ? dto.getPage() : 0;
        int size = (dto.getSize() > 0) ? dto.getSize() : 10;

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));

        Page<UserEntity> users = userRepository.findUsersWithFilters(
                searchTerm,
                active,
                verified,
                roles,
                location,
                pageable
        );

        return users.map(entity -> {
            GetUserDto dtoResult = new GetUserDto();
            mapUserEntityToDto(entity, dtoResult);
            return dtoResult;
        });
    }

    private boolean isValidSortField(String field) {
        List<String> allowedFields = List.of(
                "name",
                "surname",
                "username",
                "email",
                "active",
                "verified",
                "created_at",
                "location_id.description"
        );

        return allowedFields.contains(field);
    }

}
