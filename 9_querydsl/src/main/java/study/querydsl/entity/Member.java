package study.querydsl.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"}) // 주의: team을 넣으면 team에서도 toString있으면 서로 계속 양방향호출되서 넣어주면안된다.
// 그래서 연관관계 필드는뺴고 , 클래스 본인이 소유한 필드들만 넣어주자.
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id") // 외래키이름.
    private Team team;

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            chageTeam(team);
        }
    }

    private void chageTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

    // 위 @toString과 동일
//    @Override
//    public String toString() {
//        return "Member{" +
//                "id=" + id +
//                ", username='" + username + '\'' +
//                ", age=" + age +
//                '}';
//    }
    // 아래 생성자들은 없어도되는데 예제에서 쓸거 ( 중요한건아님 무시 )
    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public Member(String username) {
        this(username, 0);
    }
}
