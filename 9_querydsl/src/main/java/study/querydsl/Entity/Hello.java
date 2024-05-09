package study.querydsl.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
public class Hello {
    @Id @GeneratedValue
    private Long id;

}
