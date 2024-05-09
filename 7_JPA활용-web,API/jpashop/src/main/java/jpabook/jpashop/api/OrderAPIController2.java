package jpabook.jpashop.api;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.order.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.order.OrderSearch;
import jpabook.jpashop.domain.order.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderAPIController2 {

    private final OrderRepository orderRepository;

    // 1. Entity 노출
    @GetMapping("/api/v1/orders")
    public List<Order> orderV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();

            // 초기화
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }

    // 2. dto 변환
    @GetMapping("/api/v2/orders")
    public List<OrderDto> orderV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return collect;
    }
    // 3. fetch join 전략
    @GetMapping("/api/v3/orders")
    public List<OrderDto> orderV3() {
        List<Order> orders = orderRepository.findAllWithItem();

        for (Order order : orders) {
            System.out.println("order ref = " + order + " id = " + order.getId());
        }

        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return collect;
    }

    // 3.1 페치조인 + 페이징 문제 해결
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> orderV3_1(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                    @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return collect;
    }

    // 4. JPA에서 DTO 조회

    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> orderV4() {
        List<OrderQueryDto> result = orderQueryRepository.findOrderQueryDtos();
        return result;

    }

    // 5. 4번 성능 최적화 -> 쿼리 2번나감
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> orderV5() {
        List<OrderQueryDto> result = orderQueryRepository.findallByDto_optimization();
        return result;

    }

    // 6. 5번 성능최적화 쿼리 1번나감  , 단 , 페이징 불가, 조인으로 인해 중복데이터가 추가되므로 상황에따라 v5보다 느릴수도있다., 애플리케이션에서 추가작업이 크다.
    @GetMapping("/api/v6/orders")
    public List<OrderFlatDto> orderV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();
        return flats;

    }



    // ================= DATA============ //

    @Getter // @Data에 @getter, @setter 다 있다 .
    static class OrderDto {
        private Long orderId;
        private String userName;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDTO> orderItem; // Dto안에 Entity있는게 좋지않다. Entity에대한 의존을 끊어야해 !!

        public OrderDto(Order o) {
            orderId = o.getId();
            userName = o.getMember().getName();
            orderDate = o.getOrderDate();
            orderStatus = o.getStatus();
            address = o.getDelivery().getAddress();

            // Dto안에 Entity있을떄
//            o.getOrderItems().stream().forEach(order -> order.getItem().getName()); //이거안하면 orderItem이 Null로 나옴, Entity(OrderItem)도 DTO로 변환해야해.
//            orderItem = o.getOrderItems();


            // 이렇게 아래처럼 바꿔줘야해
            orderItem = o.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDTO(orderItem))
                    .collect(Collectors.toList());

        }
    }



    @Getter
    static class OrderItemDTO {
        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDTO(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
