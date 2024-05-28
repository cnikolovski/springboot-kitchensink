package app.mongodb.kitchensink.repository;

import app.mongodb.kitchensink.model.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void testSaveMember() {
        Member member = Member.builder().name("Sally").email("sally@gmail.com").phoneNumber("0987667890").build();
        Member savedMember = memberRepository.save(member);

        Optional<Member> foundSavedMember = memberRepository.findById(savedMember.getId());
        assertTrue(foundSavedMember.isPresent());
    }

    @Test
    void testExistsByEmailWhenNotFound() {
        boolean memberExists = memberRepository.existsByEmail("random");
        assertThat(memberExists).isFalse();
    }

    @Test
    @Sql("/test-add-three-members.sql")
    void testExistsByEmail() {
        boolean memberExists = memberRepository.existsByEmail("john.doe@test.com");
        assertThat(memberExists).isTrue();
    }
    @Test
    @Sql("/test-add-three-members.sql")
    void testFindAllOrderedByName() {
        List<Member> retrievedMembers = memberRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        assertThat(retrievedMembers.size()).isEqualTo(3);
        assertThat(retrievedMembers.get(0).getName()).isEqualTo("Chris Holiday");
        assertThat(retrievedMembers.get(1).getName()).isEqualTo("John Doe");
        assertThat(retrievedMembers.get(2).getName()).isEqualTo("Sally Smith");
    }
}
