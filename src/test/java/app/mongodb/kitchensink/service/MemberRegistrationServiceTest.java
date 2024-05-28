package app.mongodb.kitchensink.service;

import app.mongodb.kitchensink.exception.EmailDuplicateException;
import app.mongodb.kitchensink.exception.MemberNotFoundException;
import app.mongodb.kitchensink.model.Member;
import app.mongodb.kitchensink.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MemberRegistrationServiceTest {

    private static final String ATTRIBUTE_NAME = "name";
    private static final String NAME = "Bob Smith";
    private static final String EMAIL = "bob.smith@test.com";
    private static final String PHONE_NUMBER = "5566890410";
    private static final String MEMBER_ID = "1234567890";

    @Mock private MemberRepository memberRepository;
    @InjectMocks private MemberRegistrationService memberRegistrationService;

    @Test
    void testRegisterMember() {
        Member member = Member.builder().name(NAME).email(EMAIL).phoneNumber(PHONE_NUMBER).build();
        memberRegistrationService.register_member(member);
        verify(memberRepository).save(member);
    }

    @Test
    void testRegisterMemberWithDuplicateEmail() {
        Member member = Member.builder().name(NAME).email(EMAIL).phoneNumber(PHONE_NUMBER).build();
        given(memberRepository.existsByEmail(EMAIL)).willReturn(true);

        assertThrows(EmailDuplicateException.class, () -> memberRegistrationService.register_member(member));
        verify(memberRepository, never()).save(member);
    }

    @Test
    void testGetAllMembers() {
        Member member = Member.builder().name(NAME).email(EMAIL).phoneNumber(PHONE_NUMBER).build();
        given(memberRepository.findAll(Sort.by(Sort.Direction.ASC, ATTRIBUTE_NAME))).willReturn(Arrays.asList(member));

        List<Member> members = memberRegistrationService.getAllMembers();
        assertThat(members.size()).isEqualTo(1);
        assertThat(members.get(0).getName()).isEqualTo(NAME);
        assertThat(members.get(0).getEmail()).isEqualTo(EMAIL);
        assertThat(members.get(0).getPhoneNumber()).isEqualTo(PHONE_NUMBER);
    }

    @Test
    void testGetAllMembersEmpty() {
        List<Member> members = memberRegistrationService.getAllMembers();
        assertThat(members).isEmpty();
    }

    @Test
    void getMemberByIdNotFound() {
        Long memberId = Long.valueOf(1);
        Member member = Member.builder().name(NAME).email(EMAIL).phoneNumber(PHONE_NUMBER).build();
        given(memberRepository.findById(memberId)).willReturn(Optional.ofNullable(member));

        Member response = memberRegistrationService.getMemberById(memberId);
        assertThat(response.getName()).isEqualTo(NAME);
        assertThat(response.getEmail()).isEqualTo(EMAIL);
        assertThat(response.getPhoneNumber()).isEqualTo(PHONE_NUMBER);    }

    @Test
    void getMemberById() {
        Long memberId = Long.valueOf(1);
        assertThrows(MemberNotFoundException.class, () -> memberRegistrationService.getMemberById(memberId));
    }
}

