package study.querydsl.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

// entity 하나 만들어준뒤에 아래와같이 complie.java run해주면됨.
// -> querydsl 이  entity를 보고 Qclass 생성해줌.

@Entity
@Getter @Setter
public class Hello{
    @Id @GeneratedValue
    private Long id;

}