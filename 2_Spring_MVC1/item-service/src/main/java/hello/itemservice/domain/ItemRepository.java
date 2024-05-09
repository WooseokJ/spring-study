package hello.itemservice.domain;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController //. @Component있으므로 컴포넌트 스캔대상.
public class ItemRepository {

    private static final Map<Long, Item> store = new HashMap<>(); // 실무에선 HashMap대신에 동시성문제떄문에 ConcurrentHashMap 사용
    private static long sequence = 0L;

    // item 저장
    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    // id 로 item 찾기
    public Item findById(Long id) {
        return store.get(id);
    }

    // 전체 조회
    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    // 업데이트
    public void update(Long id, Item updateParam) { // Item대신 ItemParamDTO로 만들어서 아래 set들을 해주는것을 따로 뺴주는게 좋아.(클래스하나만들어서)
        Item findItem = findById(id);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    // test 용
    public void clearStore() {
        store.clear();
    }


}
