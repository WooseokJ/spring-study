package jpabook.jpashop.service;

import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.repository.UpdateItemDTO;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    // item 저장
    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }
    // item 수정
    @Transactional // commit되고 -> flush 날림. (변경감지 기능 사용!! 항상 이걸로 사용 권장!!)
    public void updateItem(Long itemId, UpdateItemDTO updateItemDTO) {
        Item findItem = itemRepository.findOne(itemId); // 영속 상태
        findItem.chage(updateItemDTO);
    }
    // item 모두 조회
    public List<Item> findItems() {
        return itemRepository.findAll();
    }
    // item id로 조회.
    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
