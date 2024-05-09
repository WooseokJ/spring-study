package hello.servlet.web.frontcontroller.V4.controller;

import hello.servlet.domain.Member;
import hello.servlet.domain.MemberRepository;
import hello.servlet.web.frontcontroller.V4.ControllerV4;

import java.util.Map;

public class MemberSaveControllerV4 implements ControllerV4 {
    MemberRepository repository = MemberRepository.getInstance();


    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        String username = paramMap.get("username");
        String ageString = paramMap.get("age");
        int age = Integer.parseInt(ageString);
        Member member = new Member(username, age);
        repository.save(member);

        model.put("member", member); // 빈 model에 데이터 넣어줘
        return "save-result";
    }
}
