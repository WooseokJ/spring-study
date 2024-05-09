package hello.itemservice.repository.mybatis;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;


// mybaits 매핑 Xml을 호출해주는 매퍼 인터페이스.
@Mapper  // mybatis에서 인식하기위함. (단, xml파일의 위치를 패키지명(hello.itemservice.repository.mybatis)에 맞춰줘야해)
public interface ItemMapper { // 구현체는 자동으로 만들어줘.
    void save(Item item);
    void update(@Param("id") Long id,
                @Param("updateParam")ItemUpdateDto updateParam);

    List<Item> findAll(ItemSearchCond itemSearch);

    Optional<Item> findById(Long id);

}
