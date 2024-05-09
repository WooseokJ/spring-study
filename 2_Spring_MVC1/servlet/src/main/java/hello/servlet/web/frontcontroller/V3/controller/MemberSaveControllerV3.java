package hello.servlet.web.frontcontroller.V3.controller;

import hello.servlet.domain.Member;
import hello.servlet.domain.MemberRepository;
import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.V3.ControllerV3;

import java.util.Map;

public class MemberSaveControllerV3 implements ControllerV3 {

    MemberRepository repository = MemberRepository.getInstance();

    @Override
    public ModelView process(Map<String, String> paramMap) {
        String username = paramMap.get("username");
        String ageString = paramMap.get("age");
        int age = Integer.parseInt(ageString);
        Member member = new Member(username, age);
        repository.save(member);


        ModelView modelView = new ModelView("save-result");
        modelView.getModel().put("member", member);
        return  modelView;

    }
}
