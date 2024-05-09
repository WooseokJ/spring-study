package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.order.Order;
import jpabook.jpashop.domain.order.OrderSearch;
import jpabook.jpashop.domain.order.OrderStatus;
import jpabook.jpashop.repository.simpleQuery.SimpleOrderQueryDto;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.simpleQuery.SimpleOrderQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/*

  1:1, N:1 에서의 성능최적화
* Order 조회 1번
* Order -> Member 지연로딩 N 번
* Order -> Delivery 지연로딩 N 번

*
* */
@RestController
@RequiredArgsConstructor
public class OrderSimpleAPIController {

    private final OrderRepository orderRepository;

    // 1. Entity 직접 노출
    @GetMapping("/api/v1/simple-orders")
    public List<Order> orderV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); // Lazy강제 초기화
            order.getDelivery().getAddress(); // Lazy강제 초기화
        }
        return all;
    }
    // 2. DTO 변환
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> orderV2() {
        // order -> 1번 SQL 나가 -> 결과 2개
        // 루프 돌떄
        // 1 + N 문제 , 처음 쿼리 결과로 n(2)개 orders 가져옴 추가로 delivery n(5)번
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return result;

    }

    // 3. 페치조인 DTO 변환
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return result;
    }


    // 4. jpa에서 바로 DTO 조회( 3, 4 번 우열을 가리기 힘듬. 3번은 전체 select, 4번은 원하는것만 select )
    private final SimpleOrderQueryRepository simpleOrderQueryRepository;

    @GetMapping("/api/v4/simple-orders")
    public List<SimpleOrderQueryDto> orderV4() {
        return simpleOrderQueryRepository.findOrderDtos();
    }


    @Data
//    @AllArgsConstructor  // 이거로하면 생성자 생성떄마다 id,name,date 등등 계속 넣어줘야해서 안좋다.
    static class SimpleOrderDto {
        private Long orderId;
        private String username;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            username = order.getMember().getName(); // Lazy 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // Lazy 초기화
        }
    }




}
