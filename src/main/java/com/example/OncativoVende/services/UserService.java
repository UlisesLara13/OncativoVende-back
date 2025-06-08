package com.example.OncativoVende.services;

import com.example.OncativoVende.dtos.get.GetUserDto;
import com.example.OncativoVende.dtos.post.*;
import com.example.OncativoVende.dtos.put.PutPersonalDataDto;
import com.example.OncativoVende.dtos.put.PutUserDto;
import com.example.OncativoVende.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    List<GetUserDto> getAllUsers();

    GetUserDto getUserById(Integer id);

    GetUserDto createUser(PostUserDto postUserDto);

    GetUserDto updateUser(PutUserDto putUserDto, Integer id);

    GetUserDto updatePersonalData(PutPersonalDataDto putPersonalDataDto, Integer id);

    boolean updateAvatarUrl(String avatarUrl, Integer id);

    void deleteUser(Integer id);

    void activeUser(Integer id);

    void verifyUser(Integer id);

    void unverifyUser(Integer id);

    GetUserDto getUserByEmail(String email);

    GetUserDto verifyLogin(PostLoginDto postLoginDto);

    void changePassword(ChangePassword changePassword);

    void resetPassword(ResetPasswordDto dto);

    void sendRecoveryCode(RecoveryRequestDto request);

    Page<GetUserDto> filterUsers(UserFilterDto dto);

    void mapUserEntityToDto(UserEntity userEntity, GetUserDto getUserDto);

}
