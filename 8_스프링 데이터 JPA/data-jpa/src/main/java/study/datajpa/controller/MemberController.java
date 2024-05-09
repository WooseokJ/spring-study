package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    // ( 도메인 클래스 컨버터 사용 전)
    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    // 스프링이 자동으로 위에꺼를 해줌.( 도메인 클래스 컨버터 사용 후)
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }



    @PostConstruct // DI 이후 초기화 수행 메서드 (service 수행전 발생)
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user" + i , i));
        }
    }

    // 페이징과 정렬
    @GetMapping("/members/v1")
    public Page<Member> listV1(@PageableDefault(size = 5,sort = "id") Pageable pageable) {
        Page<Member> memberPage = memberRepository.findAll(pageable); // 스프링 데이터 jpa가 지원.
        return memberPage;
    }
    // /members?page=0 하면 20개까지만 꺼냄. default는 20개
    // /members?page=0&size=3 하면 3개까지만 꺼냄.
    // /members?page=0&size=3&sort=id,desc 하면 3개까지만 꺼냄(id 내림차순) default는 asc

    // member -> dto로 변환해 반환
    @GetMapping("/members/v2")
    public Page<MemberDto> listV2(@PageableDefault(size = 5,sort = "id") Pageable pageable) {
        Page<Member> memberPage = memberRepository.findAll(pageable); // 스프링 데이터 jpa가 지원.
        Page<MemberDto> dtoPage = memberPage.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
        return dtoPage;
    }

    @GetMapping("/members/v3")
    public Page<MemberDto> listV3(@PageableDefault(size = 5,sort = "id") Pageable pageable) {
        Page<Member> memberPage = memberRepository.findAll(pageable); // 스프링 데이터 jpa가 지원.
        Page<MemberDto> dtoPage = memberPage.map(MemberDto::new);
        return dtoPage;
    }

    // page를 1부터 나오게하고싶을떄
    @GetMapping("/members/v4")
    public Page<MemberDto> listV4(@PageableDefault(size = 5,sort = "id") Pageable pageable) {
        PageRequest request = PageRequest.of(1, 2, Sort.by("username").descending()); // 이것만추가.
        Page<Member> memberPage = memberRepository.findAll(request); // 스프링 데이터 jpa가 지원.
        Page<MemberDto> dtoPage = memberPage.map(MemberDto::new);
        return dtoPage;
    }

    
}
