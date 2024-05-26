package app.mongodb.kitchensink.repository;

import app.mongodb.kitchensink.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT member FROM Member member WHERE member.email = :email")
    Member existsByEmail(String email);

    @Query("SELECT member FROM Member member ORDER BY member.name")
    List<Member> findAllOrderedByName();
}
