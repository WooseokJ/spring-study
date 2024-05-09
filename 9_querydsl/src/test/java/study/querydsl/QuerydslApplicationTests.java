package study.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Hello;
import study.querydsl.entity.QHello;

import static org.assertj.core.api.Assertions.assertThat;

//test
@SpringBootTest
@Transactional // 테스트끝나고 롤백해줌.
@Commit
class QuerydslApplicationTests {

    @PersistenceContext //  @Autowired로 해도 동일.
    EntityManager em;

    @Test
    void contextLoads() {
        Hello hello = new Hello();
        em.persist(hello);

        JPAQueryFactory query = new JPAQueryFactory(em);
        QHello qHello = QHello.hello; // new QHello("hello") 와 동일.(QHello.hello는 알아서 만들어둔거)
        // querydsl 사용.( 이떄 관련 객체는 Q클래스 객체를 사용)
        Hello result = query
                .selectFrom(qHello)
                .fetchOne();

        assertThat(result).isEqualTo(hello);
//		lombok 동작 확인 (hello.getId())
        assertThat(result.getId()).isEqualTo(hello.getId());

    }

}