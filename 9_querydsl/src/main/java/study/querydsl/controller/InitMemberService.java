package study.querydsl.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

@Service
public class InitMemberService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void init() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);
        for(int i = 0; i< 100; i++) {
            Team selectedTeam = (i % 2 == 0) ? teamA : teamB;
            em.persist(new Member("member"+i, i, selectedTeam));
        }
    }
}
