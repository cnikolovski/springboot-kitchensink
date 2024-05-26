package app.mongodb.kitchensink.repository;

import app.mongodb.kitchensink.model.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class MemberRepositoryTest {

    private static final String NAME = "Bob Smith";
    private static final String EMAIL = "bob.smith@test.com";
    private static final String PHONE_NUMBER = "5566890410";
    private static final String NAME_ORDER_ONE = "Chris Holiday";
    private static final String NAME_ORDER_TWO = "John Doe";
    private static final String NAME_ORDER_THREE = "Sally Smith";

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void testSaveMember() {
        Member member = Member.builder().name(NAME).email(EMAIL).phoneNumber(PHONE_NUMBER).build();
        Member savedMember = memberRepository.save(member);

        Optional<Member> foundSavedMember = memberRepository.findById(savedMember.getId());
        assertTrue(foundSavedMember.isPresent());
    }

    @Test
    @Sql("/test-add-one-member.sql")
    void testExistsByEmail() {
        Member member = Member.builder().name(NAME).email(EMAIL).phoneNumber(PHONE_NUMBER).build();
        Member retrievedMember = memberRepository.existsByEmail(member.getEmail());
        assertThat(retrievedMember).isNotNull();
    }

    @Test
    @Sql("/test-add-three-members.sql")
    void testFindAllOrderedByName() {
        List<Member> retrievedMembers = memberRepository.findAllOrderedByName();
        assertThat(retrievedMembers.get(0).getName()).isEqualTo(NAME_ORDER_ONE);
        assertThat(retrievedMembers.get(1).getName()).isEqualTo(NAME_ORDER_TWO);
        assertThat(retrievedMembers.get(2).getName()).isEqualTo(NAME_ORDER_THREE);
    }
}
