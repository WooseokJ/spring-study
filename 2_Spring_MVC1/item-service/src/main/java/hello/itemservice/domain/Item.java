package hello.itemservice.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Item {
    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    public Item() {}

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
// DTO의 경우에는 @Data를 사용해도 괜찮지만
// Entity를 만들떄는 @Getter, @Setter를 쓰자.,