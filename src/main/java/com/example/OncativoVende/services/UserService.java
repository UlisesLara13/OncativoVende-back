package com.example.OncativoVende.services;

import com.example.OncativoVende.dtos.get.GetUserDto;
import com.example.OncativoVende.dtos.post.PostUserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    List<GetUserDto> getAllUsers();

    GetUserDto createUser(PostUserDto postUserDto);

}
