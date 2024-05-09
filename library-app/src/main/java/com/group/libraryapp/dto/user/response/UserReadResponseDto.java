package com.group.libraryapp.dto.user.response;

import com.group.libraryapp.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor // 모든 필드로 생성자 만들어줌. , @NoArgsConstructor는 파라미터없는 기본생성자만듬
@NoArgsConstructor
public class UserReadResponseDto {
    private Long id;
    private String name;
    private Integer age;

    public UserReadResponseDto(User user) {
        this.id  = user.getId();
        this.name = user.getName();
        this.age = user.getAge();
    }
}
