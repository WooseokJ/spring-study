package com.group.libraryapp.service.user;

import com.group.libraryapp.dto.user.request.UserUpdateRequestDto;
import com.group.libraryapp.dto.user.response.UserReadResponseDto;
import com.group.libraryapp.repository.user.UserJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceV1 {
    private final UserJdbcRepository userJdbcRepository;

    public String updateUser(UserUpdateRequestDto requestDto) {
        Boolean isUserNotExist = userJdbcRepository.isUserNotExist(requestDto.getId());
        if(isUserNotExist) {
            throw new IllegalArgumentException();
        }
        userJdbcRepository.update(requestDto.getName(), requestDto.getId());
        return "ok";
    }

    public String deleteUser(String name) {
        Boolean isUserNotExist = userJdbcRepository.isUserNotExcistDelete(name);
        if(isUserNotExist) { // 비어있으면 에러던져
            throw new IllegalArgumentException();
        }
        userJdbcRepository.delete(name);
        return "ok";
    }


    public String saveUser(String name, int age) {
        userJdbcRepository.save(name, age);
        return "ok";
    }

    public List<UserReadResponseDto> getUsers() {
        return userJdbcRepository.read();
    }
}
