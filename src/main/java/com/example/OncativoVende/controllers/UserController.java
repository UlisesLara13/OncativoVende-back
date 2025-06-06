package com.example.OncativoVende.controllers;

import com.example.OncativoVende.dtos.get.GetUserDto;
import com.example.OncativoVende.dtos.post.PostUserDto;
import com.example.OncativoVende.dtos.post.UserFilterDto;
import com.example.OncativoVende.dtos.put.PutPersonalDataDto;
import com.example.OncativoVende.dtos.put.PutUserDto;
import com.example.OncativoVende.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.OncativoVende.dtos.post.ChangePassword;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<GetUserDto>> getUsers() {
        List<GetUserDto> result = userService.getAllUsers();

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserDto> getUserById(@PathVariable Integer id) {
        GetUserDto result = userService.getUserById(id);

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<GetUserDto> createUser(@Valid @RequestBody PostUserDto postUserDto) {
        GetUserDto result = userService.createUser(postUserDto);

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<Void> activateUser(@PathVariable Integer id) {
        userService.activeUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/verify/{id}")
    public ResponseEntity<Void> verifyUser(@PathVariable Integer id) {
        userService.verifyUser(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/unverify/{id}")
    public ResponseEntity<Void> unverifyUser(@PathVariable Integer id) {
        userService.unverifyUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetUserDto> updateUser(@PathVariable Integer id, @Valid @RequestBody PutUserDto putUserDto) {
        GetUserDto result = userService.updateUser(putUserDto, id);

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/personal-data/{id}")
    public ResponseEntity<GetUserDto> updateUser(@PathVariable Integer id, @Valid @RequestBody PutPersonalDataDto putPersonalDataDto) {
        GetUserDto result = userService.updatePersonalData(putPersonalDataDto, id);

        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }


    @PutMapping("/avatar/{id}")
    public ResponseEntity<Void> updateAvatarUrl(@PathVariable Integer id, @RequestBody String avatarUrl) {
        userService.updateAvatarUrl(avatarUrl, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePassword changePassword) {
        userService.changePassword(changePassword);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/filter")
    public Page<GetUserDto> filterUsers(@RequestBody UserFilterDto dto) {
        return userService.filterUsers(dto);
    }

}
