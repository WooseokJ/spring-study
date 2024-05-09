package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range; // hibernate validator 구현체
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank; // javax가 제공하는 표준인터페이스
import javax.validation.constraints.NotNull;

@Data
//@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000", message = "총합 10000원 넘게 입력해줘") // 가격 * 수량이 10000원을 넘어야한다.
public class Item {

//    @NotNull(groups = UpdateCheck.class) // 수정 요구사항 추가(@NotNull), 수정시에만 @notNull 검증 적용.
    private Long id;

//    @NotBlank(message = "공백x", groups = {SaveCheck.class, UpdateCheck.class}) // 빈값,공백("", " ")만 있는경우 허용 x
    private String itemName;

//    @NotNull(groups = {SaveCheck.class, UpdateCheck.class}) // null 허용 x
//    @Range(min = 1000, max = 10000, groups = {SaveCheck.class, UpdateCheck.class}) // 값이 1000이상~10000이하 범위의있어야해
    private Integer price;

//    @Max(value = 9999, groups = SaveCheck.class) // 최대 9999까지만 허용.
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
