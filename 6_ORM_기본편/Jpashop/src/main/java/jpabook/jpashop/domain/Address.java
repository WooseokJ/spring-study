package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    // 의미단위로 메서드 만들수도있다.
    public String fullAddress() {
        return getCity() + getStreet() + " " + getZipcode();
    }
}
