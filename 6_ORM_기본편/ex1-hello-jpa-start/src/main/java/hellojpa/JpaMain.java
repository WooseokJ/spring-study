package hellojpa;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        // EntityManager는 데이터 변경시 트랜잭션 시작해야함.
        tx.begin();
        try {

            /* 1.
            엔티티를 생성한 상태(비영속)
            Member member = new Member();
            member.setId(1L);
            member.setName("helloA");

            회원 엔티티 영속상태로 만듬.
            em.persist(member);
            === 여기까지는 Db에 insert SQl 안보냄.===

            커밋하는 순간 DB에 insert sql 보냄.
            tx.commit();


            만약 em.persist이후 바로 DB에 반영하려면
            em.flush();

            **/


            /* 2.
            영속 엔티티 조회
            Member member = em.find(Member.class, 1L);

            영속 엔티티 데이터 수정
            member.setName("helloA");
            tx.commit();

            그런데 em.update(member) 같은 코드있어야하지않나?
            -> 변경감지로인해 필요없다.

            **/



            /* 3.
            JPQL 쿼리실행시 플러시가 자동으로 호출
            List<Member> result = em.createQuery("select m from Member as m", Member.class)
                                    .setFirstResult(1)
                                    .setMaxResults(10)
                                    .getResultList();
            for(Member member:result)
                System.out.println("member.getName = " + member.getName());
            tx.commit();

            **/



            /* 4.
            em.detach(entity) : 특정 엔티티만 준영속상태로 전환
            em.clear(): 영속성 컨텍스트 완전 초기화
            em.close(): 영속성 컨텍스트 종료.

            **/

            // 연관관계 매핑 기초
            /*
            //팀 저장
            Team team = new Team();
            team.setId(1L);
            team.setName("teamA");
            em.persist(team);
            // 회원저장
            Member member = new Member();
            member.setId(1L);
            member.setUsername("member1");
            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member.getId());
            List<Member> members = findMember.getTeam().getMembers();
            for(Member m: members)
                System.out.println("m= "+m.getUsername());


            tx.commit();
            */

            /* 프록시
            Member member = new Member();
            member.setUsername("hello");
            em.persist(member);
            em.flush();
            em.clear();
            Member m1 = em.find(Member.class, member.getId()); // m1.getClass(): Member
            Member m2 = em.getReference(Member.class, member.getId()); // m2.getClass() : 프록시


            System.out.println("m1 == m2: " + (m1 == m2));
            System.out.println("m1 == m2: " + (m1 instanceof  Member));
            System.out.println("m1 == m2: " + (m2 instanceof  Member));
            */

            /*임베디드타입*/

//            Member member = new Member();
//            member.setUsername("hes");
//            member.setHomeAddress(new Address("city", "street", "zipcode"));
//            member.setWorkPeriod(new Period());
//            em.persist(member);
//            tx.commit();

            /*
            Address address = new Address("city", "street", "10000");
            Member member = new Member();
            member.setUsername("m1");
            member.setHomeAddress(address);
            em.persist(member);

            Address newAddress = new Address(address.getCity(), address.getStreet(), address.getZipcode());


            Member member1 = new Member();
            member1.setUsername("m2");
            member1.setHomeAddress(newAddress);
            em.persist(member1);

            member.getHomeAddress().setCity("newCity");
             */

            Member member = new Member();
            member.setHomeAddress(new Address("homeCity", "street", "100"));
            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("피자");

            member.getAddressesHistory().add(new Address("old1", "street", "100"));
            member.getAddressesHistory().add(new Address("old2", "street", "100"));
            em.persist(member);

            em.flush();
            em.clear();
            Member findMember = em.find(Member.class, member.getId()); // 값타입 컬렉션은 지연로딩.(쿼리문안날라가), 일반 임베디드는 즉시로딩임.
            Set<String> favoriteFoods = findMember.getFavoriteFoods();// 이제 쿼리문날라감.
            for (String favoriteFood : favoriteFoods)
                System.out.println(favoriteFood);

            // 수정
            findMember.getFavoriteFoods().remove("치킨");
            findMember.getFavoriteFoods().add("한식");

            findMember.getAddressesHistory().remove(new Address("old1", "street", "100"));
            findMember.getAddressesHistory().add(new Address("newOld", "street", "100"));




        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }


    }
}
