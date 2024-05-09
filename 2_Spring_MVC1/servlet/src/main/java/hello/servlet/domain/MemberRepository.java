package hello.servlet.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// 현재 동시성 문제가 고려되어있지않다. 실무에선 ConcureentHashMap, AtomicLong 사용을 하자.

public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    // 싱글턴
    private static final MemberRepository instance = new MemberRepository(); // 싱글턴
    private MemberRepository() {} // 객체 생성 방지.(싱글턴과 짝궁)

    public static MemberRepository getInstance() {
        return  instance;
    }
    // 회원 저장.
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member); // id: member 형태
        return member;
    }
    // id 로 회원찾기
    public Member findById(Long id ) {
        return store.get(id);
    }
    // 회원 모두 찾기
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    // 테스트에서 쓰기위해 store 모두제거
    public void clearStore() {
        store.clear();
    }



}
