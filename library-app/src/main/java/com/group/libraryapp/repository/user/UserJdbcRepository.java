package com.group.libraryapp.repository.user;

import com.group.libraryapp.dto.user.response.UserReadResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void update(String name, Long id) {
        String sql = "update user set name = ? where id = ?";
        jdbcTemplate.update(sql, name, id);
    }

    public Boolean isUserNotExist(Long id) {
        String readSql = "select * from user where id = ?";
        List<Integer> userList = jdbcTemplate.query(readSql, (rs, rowNum) -> 1234, id);
        return  userList.isEmpty();
    }


    // delete
    public Boolean isUserNotExcistDelete(String name) {
        String readSql = "select * from user where name = ?";
        return jdbcTemplate.query(readSql, (rs, rowNum) -> 1234, name).isEmpty();
    }

    public void delete(String name) {
        String sql = "delete from user where name = ?";
        jdbcTemplate.update(sql, name);
    }

    // create
    public void save(String name, int age) {
        String sql = "insert into user (name, age) values (?, ?)";
        jdbcTemplate.update(sql, name, age);
    }
    // read
    public List<UserReadResponseDto> read() {
        String sql = "select * from user";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            return new UserReadResponseDto(id, name, age);
        });

    }

}
