package hellojpa.jpql;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
//            Member member = new Member();
//            member.setUsername("m1");;
//            member.setAge(10);
//            em.persist(member);
//            em.flush();
//            em.clear();
//
//            // 여러값 조회-이름,나이
//
//            // 방법 1: Query 타입으로 조회.
//            List resultList1 = em.createQuery("select m.username,m.age from Member m")
//                    .getResultList();
//
//            Object[] result = (Object[]) resultList1.get(0);
//            System.out.println("usernae = " + result[0]); //  username = m1;
//            System.out.println("age = " + result[1]); // age = 10;
//
//            // 방법 2 : Obejct[] 타입으로 조회
//            List<Object[]> resultList2 = em.createQuery("select m.username,m.age from Member m", Object[].class)
//                    .getResultList();
//            for (Object[] objects : resultList2)
//                System.out.println("username = "+objects[0]); // username = m1;
//
//            // 방법3: new 명령어로 조회(권장)
//
//            List<MemberDto> resultList3 = em.createQuery("select new hellojpa.jpql.MemberDto(m.username,m.age) from Member m", MemberDto.class)
//                    .getResultList();
//            for (MemberDto memberDto : resultList3) {
//                memberDto.getUsername();
//            }
//
//            // 페이징 api
//            for(int i = 0; i < 5; i++) {
//                Member member1 = new Member();
//                member1.setUsername("m"+i);
//                member1.setAge(i);
//                em.persist(member1);
//            }
//
//            List<Member> resultList = em.createQuery("select m from Member m order by m.age desc ", Member.class)
//                    .setFirstResult(0)
//                    .setMaxResults(5)
//                    .getResultList();
//            for (Member member1 : resultList)
//                System.out.println("member1 = " + member1);
//
//            // 조인
//
//
//            Team team = new Team();
//            team.setName("team1");
//            em.persist(team);
//
//            Member member1 = new Member();
//            member1.chageTeam(team);
//            em.persist(member1);
//            em.flush();
//            em.clear();
//
//            String query = "select m from Member m, Team t where m.username = t.name";
//            List<Member> resultJoin = em.createQuery(query, Member.class)
//                    .setFirstResult(1)
//                    .setMaxResults(5)
//                    .getResultList();
//
//
//
//            // case 식
//            String query2 = "select "+
//                                "case when m.age <= 10 then '학생요금'"+
//                                "     when m.age <= 60 then '경로요금'"+
//                                "     else '일반요금'"+
//                                "end " +
//                                "from Member m";
//            List<String> resu = em.createQuery(query2, String.class).getResultList();
//            for (String s : resu)
//                System.out.println("s = " + s);


//            String query3 = "select func('group_concat',i.name) from Item i";
//            List<String> stringList = em.createQuery(query3, String.class).getResultList();
//            for (String s : stringList) {
//                System.out.println("s값: = " + s);
//            }

            // fetch join
            Team tA = new Team("팀A");
            Team tB = new Team("팀B");
            em.persist(tA);
            em.persist(tB);

            Member m1 = new Member("회원1", tA);
            Member m2 = new Member("회원2", tA);
            Member m3 = new Member("회원3", tB);
            em.persist(m1);
            em.persist(m2);
            em.persist(m3);

            em.flush();
            em.clear();

            String queryFetch = "select m from Member m join fetch m.team";
            List<Member> resultList4 = em.createQuery(queryFetch, Member.class).getResultList();
            for (Member m : resultList4) {
                System.out.println("username = " + m.getUsername() + "," + "teamname = " + m.getTeam().getName());
            }

            String queryCollection = "select t "+
                    "from Team t join fetch t.members "+
                    "where t.name = '팀A'";
            List<Team> resultList = em.createQuery(queryCollection, Team.class).getResultList();
            System.out.println("resultList = " + resultList.size());
            for (Team team : resultList) {
                System.out.println("team = " + team);
                for(Member member: team.getMembers())
                    System.out.println("member = " + member);
            }


            String q = "select t from Team t join fetch t.members";
            List<Team> resultListq = em.createQuery(q, Team.class)
                    .getResultList();
            for (Team team : resultListq) {
                System.out.println("team = " + team.getName());
            }


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }
}
