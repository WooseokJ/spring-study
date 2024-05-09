package hellojpa.jpql;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@ToString(exclude = {"team"})
@NoArgsConstructor
public class Member {
    @Id @GeneratedValue
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;


    public void chageTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

    public Member(String name, Team team) {
        this.username = name;
        this.team = team;
    }

}
