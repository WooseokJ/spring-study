package com.group.libraryapp.controller.user;


import com.group.libraryapp.domain.Fruit;
import com.group.libraryapp.dto.user.request.UserCreateRequstDto;
import com.group.libraryapp.dto.user.request.UserUpdateRequestDto;
import com.group.libraryapp.dto.user.response.UserReadResponseDto;
import com.group.libraryapp.service.fruit.FruitInterface;
import com.group.libraryapp.service.user.UserServiceV1;
import com.group.libraryapp.service.user.UserServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

//    private final JdbcTemplate jdbcTemplate; // jdbcTemplate를 통해 sql을 날릴수있다.
    private final UserServiceV2 userServiceV2;
    @Qualifier("main")
    private final FruitInterface fruitInterface;




    // 테스트용. (가볍게보자)
    @GetMapping("/fruit") // Fruit객체 반환하면 json으로 반환값 클라가 받음.
    public Fruit fruit() {
        return new Fruit("과일", 100);
    }


    // Create
//    @PostMapping("/user")
//    public String save(@RequestBody UserCreateRequstDto requstDto) {
//        // db사용전
////        User user = new User(requstDto.getName(), requstDto.getAge());
////        users.add(user); // 저장 (원래는 DB에 저장)
////        return "ok";
//
//        // db사용후
//        String sql = "insert into user (name, age) values (?, ?)";
//
//        // update는 sql이 insert, update, delete 쿼리에 사용할수있다.
//        // sql, ? ? 가 파라미터 순서대로 들어간다.
//        jdbcTemplate.update(sql, requstDto.getName(), requstDto.getAge()); // sql 날림,
//
//        return "ok";
//    }


    // Read
//    @GetMapping("/user")
//    public List<UserReadResponseDto> allRead() {
        // DB 사용전
//        ArrayList<UserReadResponseDto> responseDtos = new ArrayList<>();
//        for(int i = 0; i < users.size(); i++) {
//            String name = users.get(i).getName();
//            Integer age = users.get(i).getAge();
//            Long l = Long.valueOf(i + 1);
//            UserReadResponseDto object = new UserReadResponseDto(l, name, age);
//            responseDtos.add(object);
//        }
//        return responseDtos;

//        String sql = "select * from user";
//        // sql로 user정보 가져오고 dto로 만든 UserReadResponseDto로 바꿔줌.
////        List<UserReadResponseDto> result = jdbcTemplate.query(sql, new RowMapper<UserReadResponseDto>() {
////            @Override
////            public UserReadResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException { // sql 결과를 ResultSet에 받음.
////                //UserReadResponseDto 의 값을 각각 넣어줌.
////                long id = rs.getLong("id");
////                String name = rs.getString("name");
////                int age = rs.getInt("age");
////                return new UserReadResponseDto(id, name, age);
////            }
////        });
//
//        // 리팩토링(람다로바꿈)
//        List<UserReadResponseDto> result = jdbcTemplate.query(sql, (resultSet, rowNum) -> {
//            long id = resultSet.getLong("id");
//            String name = resultSet.getString("name");
//            int age = resultSet.getInt("age");
//
//            return new UserReadResponseDto(id, name, age);
//        });
//
//        return result;
//    }

//    // update1
//    @PutMapping("/user")
//    public String update(@RequestBody UserUpdateRequestDto requestDto) {
//        String readSql = "select * from user where id = ?";
//        // readSql자리에 id = requestDto.getId()
//        // (rs,rowNum) -> 1234 은 sql 의 결과가 있으면 1234 반환.
//        // query는 반환값을 List로 반환해줌.
//
//        List<Integer> userList = jdbcTemplate.query(readSql, (rs, rowNum) -> 1234, requestDto.getId());
//
//        // userList에 해당 id갖는 유저있으면 1234가 List에 담겨있고, 유저없으면 [] 나옴.
//        boolean isUserNotExist = userList.isEmpty();
//        if(isUserNotExist) { // 비어있으면 에러던져
//            throw new IllegalArgumentException();
//        }
//        String sql = "update user set name = ? where id = ?";
//        jdbcTemplate.update(sql, requestDto.getName(), requestDto.getId());
//        return "ok";
//    }





    // delete
//    @DeleteMapping("/user")
//    public String delete(@RequestParam String name ) {
//        String readSql = "select * from user where name = ?";
//        boolean isUserNotExist = jdbcTemplate.query(readSql, (rs, rowNum) -> 1234, name).isEmpty();
//        if(isUserNotExist) { // 비어있으면 에러던져
//            throw new IllegalArgumentException();
//        }
//
//        String sql = "delete from user where name = ?";
//        jdbcTemplate.update(sql, name);
//        return "ok";
//    }


    // save 코드 리팩토링
    @PostMapping("/user")
    public void save(@RequestBody UserCreateRequstDto requstDto) {
        userServiceV2.saveUser(requstDto);
    }

    @GetMapping("/user")
    public List<UserReadResponseDto> allRead() {

        return userServiceV2.getUsers();

    }


    // update 코드 리팩토링
    @PutMapping("/user")
    public void updateRefactor(@RequestBody UserUpdateRequestDto requestDto) {
        userServiceV2.updateUser(requestDto); //
    }

    // delete 코드 리팩토링
    @DeleteMapping("/user")
    public void delete(@RequestParam String name) {
        userServiceV2.deleteUser(name);
    }



    // 예외 그냥 던지기
    @GetMapping("/user/error-test")
    public void errorTest() {
        throw new IllegalStateException();
    }


}
