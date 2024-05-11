package study.querydsl.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.querydsl.dto.MemberCond;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.repository.MemberRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/v1/members")
    public List<MemberTeamDto> searchMemberV1(MemberCond cond) {
        return memberRepository.searchByWhere(cond);
    }
    //
    // http://localhost:8080/v1/members?teamName=teamB 이렇게하면 해당조건에 맞게 필터된다.
}
