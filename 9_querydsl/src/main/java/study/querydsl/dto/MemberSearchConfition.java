package study.querydsl.dto;

import lombok.Data;

@Data
public class MemberSearchConfition {
    private String username;
    private String teamName;
    private Integer ageGoe;// 나이가 이상
    private Integer ageLoe; // 나이가 이하.

}
