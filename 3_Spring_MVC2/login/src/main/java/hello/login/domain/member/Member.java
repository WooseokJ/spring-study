package hello.login.domain.member;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Member {
    private Long id; // db에 저장할 id

    @NotEmpty
    private String loginId; // 로그인 id

    @NotEmpty
    private String name; // 유저 이름.

    @NotEmpty
    private String password;



}
