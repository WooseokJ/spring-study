package jpabook.jpashop.domain.Item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.category.Category;
import jpabook.jpashop.repository.UpdateItemDTO;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();


    //=== 비지니스 로직=== //

    // stock수량 증가
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    // stock수량 감소
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if( restStock < 0 ){
            throw new NotEnoughStockException("stock 더 필요");
        }
        this.stockQuantity = restStock;
    }

    // 수정 변경 감지
    public void chage(UpdateItemDTO updateItemDTO) {
        this.setPrice(updateItemDTO.getPrice());
        this.setName(updateItemDTO.getName());
        this.setStockQuantity(updateItemDTO.getStockQuantity());
    }


}
