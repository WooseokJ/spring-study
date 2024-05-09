package hello.itemservice.web.basic;

import hello.itemservice.domain.Item;
import hello.itemservice.domain.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor

public class BasicItemController {
    private final ItemRepository itemRepository;

    // @RequiredArgsConstructor가 있으면 아래 생성자 자동으로 만들어줌.
//    @Autowired
//    public BasicItemController(ItemRepository itemRepository) {
//        this.itemRepository = itemRepository;
//    }

    @GetMapping
    public String items(Model model) { // list 보는 페이지
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    // 테스트용 데이터 추가
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 100, 1));
        itemRepository.save(new Item("itemB", 200, 1));

    }


    @GetMapping("/{itemId}")  // item 상세 보기
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add") // Get요청 : item 상품 등록
    public String addForm() {
        return "basic/addForm";
    }

    // post 요청 item 상품 등록(@RequestParam사용)
//    @PostMapping("/add") // 같은 url이여도 get, post이냐 따라 메서드 실행이 다르다.
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam Integer price,
                       @RequestParam Integer quantity,
                       Model model) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);
        itemRepository.save(item);

        model.addAttribute("item", item);


        return "basic/item";
    }

    // post 요청 item 상품등록(@ModelAttribute사용.)
//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, // 만약 "item"지우면 모델명 Item -> item 으로 첫글자 소문자로해서 자동으로 들어감.
                                                                // @ModelAttribue 생략도 가능.(custom객체라서 ReqeustParam아닌 @ModelAttribute적용.)
                            Model model) { // @ModelAttribute가 해주므로 Model 객체도 필요없다.
        itemRepository.save(item);

//        model.addAttribute("item", item); // 없어도된다.( @ModelAttribute에서 Model객체에 값넣어주고, 모델에 객체 넣어줌.)
        return "basic/item";
    }


//    @PostMapping("/add")
    public String addItemV3(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();
    }

    @PostMapping("/add")
    public String addItemV4(Item item, RedirectAttributes redirectAttributes) {
        Item saveItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId",saveItem.getId());
        redirectAttributes.addAttribute("status", true); // query파라미터형태로 들어감. &status=true
//        return "redirect:/basic/items/" + item.getId();
        return "redirect:/basic/items/{itemId}"; //redirectAttributes에 넣은 값으로 치환.
        // 8080:items/3?status=true 로 url이 반환됨. (3은 예시)
    }


    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    // redirect로 경로이동.
    // 마지막에 뷰 템플릿 호출하지않고 상품상세페이지 이동하게 리다이렉트 호출한다. (spring은 redirect: 경로  로 편리하게 리다이렉트 지원.)
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return  "redirect:/basic/items/{itemId}";
    }

    // html-form 전송은 get, post만 지원가능 / put patch는 http api 전송시에 사용.
    // prg 패턴:  상품등록시 중보처리 막는 패턴
    // 만약 상품등록하고 웹브라우저 새로고침하면 상품이 새로 등록된다.
}
