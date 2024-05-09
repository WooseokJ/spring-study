package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.order.Order;
import jpabook.jpashop.domain.order.OrderStatus;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        // given
        Member member = createMember();
        Item book = createBook("jpa1", 10000, 10);

        int orderCount = 2;
//
//        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
//        // then
        Order getOrder = orderRepository.findOne(orderId);
        Assertions.assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품주문상태는 Order");
        Assertions.assertEquals(1, getOrder.getOrderItems().size(), "주문 상품종류수가 정확해야한다.");
        Assertions.assertEquals(10000 * orderCount, getOrder.getTotalPrice(), "주문 가격은 가격 * 수량이다");
        Assertions.assertEquals(8, book.getStockQuantity(), "주문 수량만큼 재고 줄어야한다.");

    }

    private Item createBook(String name, int price, int quantity) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(quantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "경기", "11-11"));
        em.persist(member);
        return member;
    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {
        Member member = createMember();
        Item book = createBook("jpa1", 10000, 10);

        int orderCount = 11;

        //when
        Assertions.assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), book.getId(), orderCount);
        });
        //then
//        fail("재고 수량 부족 예외 발생해야함.");

    }


    @Test
    public void 주문취소() throws Exception {
        Member member = createMember();
        Item book = createBook("jpa1", 10000, 10);
        int orderCount = 2 ;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        // then
        Order getorder = orderRepository.findOne(orderId);

        Assertions.assertEquals(OrderStatus.CANCEL,getorder.getStatus(), "주문 취소시 상태는 cancel이다." );
        Assertions.assertEquals(10, book.getStockQuantity(), "주문이 취소된 상품은 그만큼 재고가 증가해야함." );



    }



}