package jpabook.jpashop.api;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController //@Controller @ResponseBody가 합쳐진거, data를 json, xml로 보내자 할떄 쓰는 어노테이션
@RequiredArgsConstructor
public class MemberAPIController {

    private final MemberService memberService;

    // 1. 회원 create API

    @PostMapping("/api/v1/members") // entity 직접 request 받음
    public CreateMemberResponseDto saveMemverV1(@RequestBody @Valid Member member) { // @RequsetBody는 json으로 온 body를 Member로 바꿔줌.,
        Long memberId = memberService.join(member);
        return new CreateMemberResponseDto(memberId);
    }

    @PostMapping("api/v2/members") // dto로 request 받음(실무)
    public CreateMemberResponseDto saveMemberV2(@RequestBody @Valid MemberAPIController.CreateMemberRequestDto request) { // 실무에서는
        Member member = new Member();
        member.setName(request.getName());

        Long memberId = memberService.join(member);
        return new CreateMemberResponseDto(memberId);
    }


    // 2. 회원 update API
    @PutMapping("api/v2/members/{id}") // 이름 수정
    public UpdateMemberResponseDto updateMemberV2(@PathVariable("id") Long id,
                                                  @RequestBody @Valid UpdateMemberRequestDTO request) {

        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponseDto(findMember.getId(), findMember.getName());

    }


    // 3. 회원 read API
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result membersV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDTO> collect = findMembers.stream()
                .map(m -> new MemberDTO(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect.size(), collect);
    }





    // ============= requestDto, responseDto ==============   //

    // 등록
    @Data
    static class CreateMemberResponseDto {
        private Long id;

        public CreateMemberResponseDto(Long id) {
            this.id = id;
        }

    }

    @Data // entity -> DTO
    static class CreateMemberRequestDto {
        @NotEmpty(message = "이름 넣어줘")
        private String name;
    }

    // 수정
    @Data
    @AllArgsConstructor // 생성자 자동으로 만들어줘
    static class UpdateMemberResponseDto {
        private Long id;
        private String name;
    }

    @Data
    static class UpdateMemberRequestDTO {
        private String name;
    }

    //조회
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDTO {

        private String name; // 필요한것만 노출
    }


}
