package hello.itemservice.domain.item;

import hello.itemservice.domain.Item;
import hello.itemservice.domain.ItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ItemRepositoryTest {
    ItemRepository itemRepository = new ItemRepository();

    @AfterEach
    void afterEach() {
        itemRepository.clearStore();
    }
    //given
    // when
    //then

    @Test
    void save() {
        //given
        Item itemA = new Item("itemA", 10000, 10);
        // when
        Item saveItem = itemRepository.save(itemA);
        //then
        Item findItem = itemRepository.findById(itemA.getId());
        assertThat(findItem).isEqualTo(saveItem);
    }

    @Test
    void findAll() {

        //given
        Item item1 = new Item("item1", 10000, 10);
        Item item2 = new Item("item2", 10000, 20);

        itemRepository.save(item2);
        itemRepository.save(item1);

        // when
        List<Item> result = itemRepository.findAll();

        //then
        for (Item item : result) {
            System.out.println(item.getItemName());
        }
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(item1, item2);
    }

    @Test
    void updateItem() {
        //given
        Item item = new Item("item1", 10000, 10);
        Item saveItem = itemRepository.save(item);
        Long itemId = saveItem.getId();

        // when
        Item updateParam = new Item("item2", 120000, 20);
        itemRepository.update(itemId, updateParam);

        //then
        Item findItem = itemRepository.findById(itemId);
        assertThat(findItem.getItemName()).isEqualTo(updateParam.getItemName());
        assertThat(findItem.getPrice()).isEqualTo(updateParam.getPrice());
    }
}