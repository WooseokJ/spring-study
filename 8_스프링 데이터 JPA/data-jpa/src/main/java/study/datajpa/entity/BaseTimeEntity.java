package study.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class) // event 기반으로 동작.
@MappedSuperclass
@Getter
public class BaseTimeEntity {
    @CreatedDate // 생성날자 넣어줌.
    @Column(updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate // 마지막 수정날자 넣어줌.
    private LocalDateTime lastModiriedDate;

}
