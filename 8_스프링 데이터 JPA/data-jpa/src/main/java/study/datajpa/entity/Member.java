package study.datajpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // protected Member() {} 대신만들어줌.
//@ToString(of = {"id", "username","age"})
//@NamedQuery(
//        name = "Member.findByUsername",
//        query = "select m from Member m where m.username =:username"
//)
//@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
public class Member extends BaseEntity {
    @Id @GeneratedValue // @GeneratedValue는 jpa가 알아서 pk 값 넣어줘(순차적으로)
    @Column(name = "member_id") // 테이블명_id 로 DB 매핑
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = LAZY) // Lazy로 해두면 Member조회시 Team은 조회안해. team은 가짜객체를 두고 team을 사용할떄 꺼내는 sql문이 별도로 날라간다.
    @JoinColumn(name = "team_id")
    private Team team;


//    protected Member() {} // @Enity는 기본생성자가 필요하다., jpa가 프록시 기술쓰는데 그떄 사용하려고 protected까지는 열어줘야해.

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if(team != null) {
            chageTeam(team);
        }
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    // 실제로는 setter보다 이게 더 낫다.
    public void chageTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
