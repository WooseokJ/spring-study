package jpabook.jpashop.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jpabook.jpashop.domain.order.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String name;

    @Embedded
    private Address address;

    @JsonIgnore // json 반환시 이거 빠짐. (근데 좋은방법이 아니다, 다른요청에서 이게 필요한경우도있어서 )
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

}
