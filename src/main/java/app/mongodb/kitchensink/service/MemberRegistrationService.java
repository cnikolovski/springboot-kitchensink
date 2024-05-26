package app.mongodb.kitchensink.service;

import app.mongodb.kitchensink.exception.EmailDuplicateException;
import app.mongodb.kitchensink.model.Member;
import app.mongodb.kitchensink.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import app.mongodb.kitchensink.exception.MemberNotFoundException;

import java.util.List;

import static java.lang.String.format;

@Service
public class MemberRegistrationService {

    private final MemberRepository memberRepository;
    private static final Logger logger = LoggerFactory.getLogger(MemberRegistrationService.class);

    public MemberRegistrationService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void register_member(Member member) {
        logger.info("Registering " + member.getName());
        validateMemberDetails(member);
        memberRepository.save(member);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAllOrderedByName();
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new MemberNotFoundException(format("Member with id: %s not found", id)));
    }

    private void validateMemberDetails(Member member) {
        Member memberExists = memberRepository.existsByEmail(member.getEmail());
        if (memberExists != null) {
            throw new EmailDuplicateException("Unique Email Violation");
        }
    }
}
