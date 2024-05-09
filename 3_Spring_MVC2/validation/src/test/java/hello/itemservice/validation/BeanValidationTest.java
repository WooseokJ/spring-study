package hello.itemservice.validation;

import hello.itemservice.domain.item.Item;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class BeanValidationTest {
    @Test
    void beanValidation() {
        // 외울필요 x (스프링과 통합하면 직접 이런코드 작성안해도됨, 가볍게 참고만하자.)
        // 검증기 생성.
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        Item item = new Item();
        item.setItemName(" ");// 공백이면안되는데 공백이라 오류
        item.setPrice(0);// 범위안에없음 오류
        item.setQuantity(10000); // 9999 넘음 오류

        // 검증 실행 : 검증대상(item)을 직접 검증기에 넣고 그결과 받는다. ConstraintViolation라는 검증오류가 담김. (결과 비어있으면 검증로유없는것)
        Set<ConstraintViolation<Item>> validations = validator.validate(item);
        for (ConstraintViolation<Item> validation : validations) {
            System.out.println("validation = " + validation);
            System.out.println("validation.getMessage() = " + validation.getMessage());
        }
    }
}