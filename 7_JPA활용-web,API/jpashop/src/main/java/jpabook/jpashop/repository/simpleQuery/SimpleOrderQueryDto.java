package jpabook.jpashop.repository.simpleQuery;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SimpleOrderQueryDto {

    private Long orderId;
    private String username;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

}
