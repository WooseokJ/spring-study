package hellojpa;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    private String city;
    private String street;
    private String zipcode;
}
