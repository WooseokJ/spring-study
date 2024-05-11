package study.querydsl.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

// Profile이 local일떄 데이터 넣어둠 (초기값 넣어주려고 만든 클래스)
@Profile("local")
@Component
@RequiredArgsConstructor
public class InitMember {

    private final InitMemberService initMemberService;

    // @PostConstruct: DI이후 초기화를 수행하는 메서드
    @PostConstruct // 스프링부트실행 -> @Profile("local")을 읽기 -> @PostConstruct를 실행.
    public void init() {
        initMemberService.init();
    }


}
