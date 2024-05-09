package study.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

@MappedSuperclass // 필드들을 상속내려서 같이사용할떄 사용.
public class JpaBaseEntity {

    @Column(updatable = false) // update로 변경 못하게 함.
    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @PrePersist // persist하기전 이벤트 발생
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createDate=now;
        updateDate=now;
    }

    @PreUpdate // update 하기전 발생
    public void preUpdate() {
        updateDate = LocalDateTime.now();
    }

}
