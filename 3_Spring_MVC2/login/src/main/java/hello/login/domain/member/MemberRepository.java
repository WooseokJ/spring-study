package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequnce = 0L;

    public Member save(Member member) {
        member.setId(++sequnce);
        log.info("save member = {}", member);
        store.put(member.getId(), member);
        return member;
    }
    // db ID 로 회원찾기
    public Member findById(Long id) {
        return store.get(id);
    }

    // logInId로 회원찾기 , 회원 못찾을수도있어서
    public Optional<Member> findByLogInId(String logInId) {
        // 방법 1
//        List<Member> all = findAll();
//        for(Member m : all) {
//            if(m.getLoginId().equals(logInId)) {
//                return Optional.of(m);
//            }
//        }
//        return Optional.empty();
        // 방법 2
        return findAll().stream()
                .filter(m -> m.getLoginId().equals(logInId))
                .findFirst();

    }


    public List<Member> findAll() {
        // values들만 뽑아서 연결리스트에 넣어준거,
        Collection<Member> members = store.values();
        return new ArrayList<>(members);
    }

    // 테스트용
    public void clear() {
        store.clear();
    }




}
