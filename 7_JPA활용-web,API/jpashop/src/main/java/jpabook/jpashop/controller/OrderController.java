package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.order.Order;
import jpabook.jpashop.domain.order.OrderSearch;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    // 주문 등록 화면 이동시
    @GetMapping("/order")
    public String CreateForm(Model model) {
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();


        model.addAttribute("members", members);
        model.addAttribute("items", items);
        return "order/orderForm";
    }
    // 주문 등록 버튼 클릭시.
    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId, // @RequestParam 은 body에 {"memberId": 1} 같이 넣어서 요청할떄 씀.
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count) {

        orderService.order(memberId, itemId, count);
        return "redirect:/orders";
    }
    // 회원이름,주문상태 입력받아서 검색버튼눌러 회원목록 조회
    @GetMapping("/orders") // @ModelAttribute은 html-form으로 입력받으므로.
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);
        return "order/orderList";

    }
    // 주문 취소 버튼클릭
    @PostMapping("orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId")Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders"; // 계속 화면에 남아있음.

    }


}
