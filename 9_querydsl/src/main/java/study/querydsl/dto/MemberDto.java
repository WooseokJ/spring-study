package study.querydsl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberDto {
    private String username;
    private int age;

    @QueryProjection // 오른쪽창의 gradle -> other -> complie.java run해줌. -> dto도 Qclass생성됨.
    public MemberDto(String username, int age) {
        this.username = username;
        this.age = age;
    }

}
