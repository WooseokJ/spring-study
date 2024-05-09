package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/validation/v3/items")
@RequiredArgsConstructor
public class ValidationItemControllerV3 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v3/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/item";
    }

//    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v3/addForm";
    }

    /*
     @Validated: Item의 애노테이션을 보고 Validation이 적용된다.
     스프링 MVC가 어떻게 Bean Validator를 사용하지?
     스프링 부트는 spring-boot-starter-validation 라이브러리 통해 자동으로 LocalValidatorFactoryBean(애노테이션기반으로 검증처리해주는객체)을 글로벌Validator로 등록해줌.
     이 글로벌 validator가 @NotNull같은 애노테이션보고 검증수행하고 결과를 bindingResult에 넣어줌.
     ( 만약에, 검증오류시 fieldError, ObjectError생성해 BindingResult에 넣어줌.)
     결론: 글로벌 validator가 되려면 @Validated를 꼭 해줘야해


    * */
    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {



        //특정 필드가 아닌 복합 룰 검증( object오류일떄)
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors={} ", bindingResult);
            return "validation/v3/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v3/items/{itemId}";
    }

    // groups 사용
//    // 수정,등록 다른조건으로 검증하기위함.
//    @PostMapping("/add")
//    public String addItem2(@Validated(SaveCheck.class) @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
//
//
//
//        //특정 필드가 아닌 복합 룰 검증( object오류일떄)
//        if (item.getPrice() != null && item.getQuantity() != null) {
//            int resultPrice = item.getPrice() * item.getQuantity();
//            if (resultPrice < 10000) {
//                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
//            }
//        }
//
//        //검증에 실패하면 다시 입력 폼으로
//        if (bindingResult.hasErrors()) {
//            log.info("errors={} ", bindingResult);
//            return "validation/v3/addForm";
//        }
//
//        //성공 로직
//        Item savedItem = itemRepository.save(item);
//        redirectAttributes.addAttribute("itemId", savedItem.getId());
//        redirectAttributes.addAttribute("status", true);
//        return "redirect:/validation/v3/items/{itemId}";
//    }



    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute Item item, BindingResult bindingResult) {

        //특정 필드가 아닌 복합 룰 검증(object오류일떄)
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }
        // 다음 오류가있으면?
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "validation/v3/editForm"; // 오류나면 editForm화면으로 이동.
        }
        // 수정 정상처리
        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }

    // groups 사용
//    @PostMapping("/{itemId}/edit")
//    public String edit2(@PathVariable Long itemId, @Validated(UpdateCheck.class) @ModelAttribute Item item, BindingResult bindingResult) {
//
//        //특정 필드가 아닌 복합 룰 검증(object오류일떄)
//        if (item.getPrice() != null && item.getQuantity() != null) {
//            int resultPrice = item.getPrice() * item.getQuantity();
//            if (resultPrice < 10000) {
//                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
//            }
//        }
//        // 다음 오류가있으면?
//        if (bindingResult.hasErrors()) {
//            log.info("errors={}", bindingResult);
//            return "validation/v3/editForm"; // 오류나면 editForm화면으로 이동.
//        }
//        // 수정 정상처리
//        itemRepository.update(itemId, item);
//        return "redirect:/validation/v3/items/{itemId}";
//    }



}

