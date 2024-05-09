package hello.itemservice;

import hello.itemservice.config.*;
import hello.itemservice.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;


//@Import(MemoryConfig.class)// MemoryConfig를 설정파일로 사용.
// 여기서는 Controller 만 컴포넌트 스캔(스캔경로 = "hello.itemservice.web")을 사용하고 나머지는 직접 수동등록한다.
//@Import(JdbcTemplateV3Config.class)
@Import(JdbcTemplateV3Config.class)
@SpringBootApplication(scanBasePackages = "hello.itemservice.web")
@Slf4j
public class ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

	// 특정 프로필경우에만 해당 스프링빈을 등록한다.
	// local이라는 이름의 프로필이 사용된경우만 testDataInit이라는 스프링빈을 등록.
	@Bean
	@Profile("local")
	public TestDataInit testDataInit(ItemRepository itemRepository) {
		return new TestDataInit(itemRepository);
	}


	/*
	* 프로필이란?
	* 스프링은 로딩시점에 application.properties의 spring.profiles.actie 속성읽어 프로필 사용.
	* 이 프로필은 local, 운영,테스트 등 다양한 환경따라 다른 설정시 사용하는 정보.
	* ex) 내 pc는 내 pc의 DB에 접근, 운영환경은 운영 DB에 접근. (프로필사용시 가능)
	*
	* 환경에따라 다른 스프링빈을 등록할수도있다.
	* */


//	@Bean
//	@Profile("test")
//	public DataSource dataSource() {
//		log.info("메모리 DB 초기화");
//		DriverManagerDataSource dataSource = new DriverManagerDataSource();
//		dataSource.setDriverClassName("org.h2.Driver");
//		dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
//		dataSource.setUsername("sa");
//		dataSource.setPassword("");
//		return dataSource;
//	}
}

