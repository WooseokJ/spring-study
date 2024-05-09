package study.datajpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import study.datajpa.entity.Member;

@Data
@AllArgsConstructor
public class MemberDto {
    private Long id;
    private String username;
    private String teamName;

    // 수업
//    public MemberDto(Member member) {
//        this.id = member.getId();
//        this.username = member.getUsername();
//        this.teamName = member.getTeam().getName();
//    }
    // 내가 수정.
    public MemberDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        if(member.getTeam() == null) {
            this.teamName = null;
        } else {
            this.teamName = member.getTeam().getName();
        }
    }

}
