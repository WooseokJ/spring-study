package study.querydsl.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"}) // 보인소유 필드만, 연관관계 필드(team)는 쓰지마.
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id") // 외래키 이름.
    private Team team;


    public Member(String username, int age, Team team) {
        this.username =username;
        this.age = age;
        if( team != null) {
            chageTeam(team);
        }
    }
    public void chageTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

    public Member(String username, int age) {
        this(username, age, null);
    }
}