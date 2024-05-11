package study.querydsl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.querydsl.dto.MemberCond;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.repository.MemberRepositoryCure;
import study.querydsl.repository.springdataJPA.MemberRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepositoryCure memberRepository; // 순수 jpa
    private final MemberRepository memberRepositorySpringDataJpa; // spring data jpa


    // /v1/members?teamName=teamB 이렇게하면 해당조건에 맞게 필터된다.
    // /v1/members?teamName=teamB&ageLoe=35&ageGoe=32
    @GetMapping("/v1/members")
    public List<MemberTeamDto> searchMemberV1(MemberCond cond) {
        return memberRepository.searchByWhere(cond); // 동적쿼리
    }
    // /v2/members?page=1&size=5 이렇게 조회. default 시작은 0페이지부터 이고 만약 size=100,000,000 이여도? ->  count쿼리 날라감.(성능최적화 전)
    @GetMapping("/v2/members")
    public Page<MemberTeamDto> searchMemberV2(MemberCond cond, Pageable pageable) {
        return memberRepositorySpringDataJpa.searchSimple(cond, pageable); //동적쿼리인데 spring data jpa가 만들어둔 Page들 사용.
    }
    // /v3/members?page=1&size=5 이렇게 조회.default 시작은 0페이지부터 이고 만약 size=100,000,000 이여도? ->  count쿼리 안날라감(성능최적화 후)
    @GetMapping("/v3/members")
    public Page<MemberTeamDto> searchMemberV3(MemberCond cond, Pageable pageable) {
        return memberRepositorySpringDataJpa.searchComplex(cond, pageable);
    }
}
