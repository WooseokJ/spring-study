package com.group.libraryapp.service.user;

import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.dto.user.request.UserCreateRequstDto;
import com.group.libraryapp.dto.user.request.UserUpdateRequestDto;
import com.group.libraryapp.dto.user.response.UserReadResponseDto;
import com.group.libraryapp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// 스프링 데이터 jpa 사용시 ( sql 안씀)
@Service
@RequiredArgsConstructor

public class UserServiceV2 {
    private final UserRepository userRepository;

    // crete
    @Transactional
    public void saveUser(UserCreateRequstDto requstDto) {
        User user = new User(requstDto.getName(), requstDto.getAge());
        User saveUser = userRepository.save(user); // 저장된 유저정보 반환.
    }
    // read
    public List<UserReadResponseDto> getUsers() {
        List<User> users = userRepository.findAll();// 모든 유저정보 가져옴.
//        return users.stream()
//                .map(user -> new UserReadResponseDto(user.getId(), user.getName(), user.getAge())) // user -> UserREadResponseDto로 바꿔줌., Stream<UserReadResponseDto>
//                .collect(Collectors.toList()); // 다시 List 타입으로 변경.  List<UserReadResponseDto>


        // 람다식
        return users.stream()
                .map(UserReadResponseDto::new)
                .collect(Collectors.toList());
    }

    // update (id기준으로 확인)ㅇㅇ
    public void  updateUser(UserUpdateRequestDto requestDto) {
        // user 존재여부 검증
        // userRepository.findById  => select * from user where id = ? 쿼리가 자동으로날라감 , Opational<User> 를 반환받음.
        User user = userRepository.findById(requestDto.getId()) // optional안에 데이터 없으면 IllegalArgumentException 예외나오고 , user있으면 User객체 찾아줌.
                .orElseThrow(IllegalArgumentException::new);


        // 객체를 업데이트 해주고
        user.updateName(requestDto.getName());
        // save메서드 호출. (변경감지를통해 자동으로 update sql이 날라간다)
        userRepository.save(user);
    }


    // dete (name 기준으로 체크)

    public void deleteUser(String name) {
        User user = userRepository.findByName(name).orElseThrow(IllegalArgumentException::new);// 오류: findByName 이런건 원래 존재하지않아 (JpaRepository를 상속받은 interface에 추가한거)
        if( user == null) {
            throw new IllegalArgumentException();
        }
        userRepository.delete(user);

    }

}
