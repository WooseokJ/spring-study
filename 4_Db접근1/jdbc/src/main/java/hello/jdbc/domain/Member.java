package hello.jdbc.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Member {
    private String memberId;
    private int money;

    public Member() {}

    public Member(String memberId, int money) {
        this.memberId = memberId;
        this.money = money;
    }
}
