package com.example.OncativoVende.services;

import com.example.OncativoVende.dtos.get.GetUserDto;
import com.example.OncativoVende.dtos.post.ChangePassword;
import com.example.OncativoVende.dtos.post.PostLoginDto;
import com.example.OncativoVende.dtos.post.PostUserDto;
import com.example.OncativoVende.dtos.put.PutPersonalDataDto;
import com.example.OncativoVende.dtos.put.PutUserDto;
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

}
