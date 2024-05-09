package hellojpa;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDateTime;

@Embeddable
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Period {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public boolean isWork() {
        // 로직~
        return true;
    }
}
