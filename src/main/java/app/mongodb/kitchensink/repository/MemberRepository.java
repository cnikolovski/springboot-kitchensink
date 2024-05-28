package app.mongodb.kitchensink.repository;

import app.mongodb.kitchensink.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

}
