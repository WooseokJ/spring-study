package study.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.Entity.Hello;
import study.querydsl.Entity.QHello;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional // 테스트끝나고 롤백해줌.
@Commit
class QuerydslApplicationTests {

	@PersistenceContext
	EntityManager em;

	@Test
	void contextLoads() {
		Hello hello = new Hello();
		em.persist(hello);

		JPAQueryFactory query = new JPAQueryFactory(em);
		QHello qHello = QHello.hello; // new QHello("hello")와 같은거로 미리 만들어둔거임.
		Hello result = query
				.selectFrom(qHello)
				.fetchOne();

		assertThat(result).isEqualTo(hello);
//		lombok 동작 확인 (hello.getId())
		assertThat(result.getId()).isEqualTo(hello.getId());

	}

}
