package hello.itemservice;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@RequiredArgsConstructor
public class TestDataInit { // 리스트에 데이터 확인편하게하려고 사용.(스프링 빈에 등록해야 메서드들 호출됨)

    private final ItemRepository itemRepository;

    /**
     * 확인용 초기 데이터 추가
     */
    // 스프링 컨테이너가 완전 초기화(AOP 등) 끝내고 , 실행준비 되었을떄 발생하는 이벤트.
    // 실행준비가 되면 해당 메서드 호출.
    // 참고: @PostConstruct는 AOP 처리 이전에 호출될수있어 간혹 문제 발생. ex. @Transactional 관련된 AOP가 적용되지않은상태로 호출될수있어.
    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        log.info("test data init");
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

}
