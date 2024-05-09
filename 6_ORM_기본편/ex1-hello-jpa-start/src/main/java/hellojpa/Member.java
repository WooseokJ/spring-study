package hellojpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

//@Entity  // jpa에서 사용할 엔티티이름지정.(기본은 클래스명, 가능하면 클래스명사용.)
//@Getter @Setter
//@Table(name = "Member") // 엔티티와 매핑할 테이블 지정
//public class Member {
//    @Id @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//    @Column(name = "username", unique = true, length = 10, nullable = false)
//    private String name;
//
//    private int age;
//
//    @Enumerated(EnumType.STRING) // num쓸떄 사용. String으로해야 enum 변수명 그대로 DB에 저장.
//    private RoleType roleType;
//
//    @Temporal(TemporalType.TIMESTAMP) // Date, Calendar 매핑시 사용.
//    private Date createDate;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date lastModifiedDate;
//
//    private LocalDate  testLocalDate;// DB에 date 타입
//    private LocalDateTime testLocalDateTime; // DB에 timestamp 타입
//
//    /*
//    TemporalType.Date: 2013-10-11
//    TemporalType.Time: 11:11:11
//    TemporalType.timestamp: 2013-10-11 11:11:11
//
//    만약 LocalDate, LocalDateTime 쓸떄는 생략가능
//
//    */
//    @Lob
//    private String description;
//
//    /*
//      DB의 BLOB, CLOB 타입과 매핑
//      CLOB: String, char[], java.sql.CLOB
//      BLOB: byte[], java.sql.BLOB
//     */
//
//    // @Transient : 메모리상에 임시로 어떤값 보관하고싶을떄 사용.
//    // ex) @Transient
//    //     private Integer temp;
//
//}
//
///*
//    java8부터 지원(Local Date, LocalTime, LocalDateTime)
//    LocalDate currentDate = LocalDate.now(); // 2013-10-11
//    LocalDate currentDate = LocalDate.of(2013,10,11); // 2013-10-11
//
//    LocalTime currentTime = LocalTime.now();  // 11:11:11
//    LocalTime targetTime = LocalTime.of(12,33,35,22); // 12:32:33.0000022
//
//    LocalDateTime currentDateTime = LocalDateTime.now(); // 2013-10-11T11:11:11
//    LocalDateTime targetDateTime = LocalDateTime.of(2019, 11, 12, 12, 32,22,333); // 2019-11-12T12:32:22.00000333
//
//*/


@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    private Long id;

    @Column(name = "username")
    private String username;

////    @Column(name = "team_id")
////    private Long teamId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false)
    private Team team;


    @OneToOne
    @JoinColumn(name = "locker_id")
    private Locker locker;

    @OneToMany(mappedBy="member")
    private List<MemberProduct> memberProducts = new ArrayList<>();




    // 임베디드타입
    // perriod
    @Embedded
    private Period workPeriod;

    // address
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "home_city")),
            @AttributeOverride(name = "street", column = @Column(name = "home_street")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "home_zipcode")),
    })
    private Address homeAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "work_city")),
            @AttributeOverride(name = "street", column = @Column(name = "work_street")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "work_zipcode")),
    })
    private Address workAddress;


    //값타입 컬렉션
    @ElementCollection
    @CollectionTable(name = "FAVORITE_FOOD",
            joinColumns = @JoinColumn(name = "MEMBER_ID") //외래키로 찾게됨.
    )
    @Column(name = "FOOD_NAME") // String 하나이므로 예외적으로 변경가능.
    private Set<String> favoriteFoods = new HashSet<>();
    @ElementCollection
    @CollectionTable(name = "ADDRESS",
            joinColumns = @JoinColumn(name = "MEMBER_ID")
    )
    private List<Address> addressesHistory = new ArrayList<>();

}