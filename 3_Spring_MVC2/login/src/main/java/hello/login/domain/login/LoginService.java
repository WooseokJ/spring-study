package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;

    // 로그인 시도(return null이면 로그인 실패.)
    public Member login(String loginId, String pw) {
        Optional<Member> findMember = memberRepository.findByLogInId(loginId);
        Member member = findMember.filter(m -> m.getPassword().equals(pw))
                .orElse(null); // 조건 만족안하면(찾는객체의 pw와 입력받은 pw가 안같으면) null.
        return member;
    }
}
