package app.mongodb.kitchensink.service;

import app.mongodb.kitchensink.exception.EmailDuplicateException;
import app.mongodb.kitchensink.model.Member;
import app.mongodb.kitchensink.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import app.mongodb.kitchensink.exception.MemberNotFoundException;

import java.util.List;

import static java.lang.String.format;

@Service
public class MemberRegistrationService {

    private static final String ATTRIBUTE_NAME = "name";
    private final MemberRepository memberRepository;
    private static final Logger logger = LoggerFactory.getLogger(MemberRegistrationService.class);

    public MemberRegistrationService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void register_member(Member member) {
        logger.info("Registering " + member.getName());
        validateMemberDetails(member);
        memberRepository.insert(member);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll(Sort.by(Sort.Direction.ASC, ATTRIBUTE_NAME));
    }

    public Member getMemberById(String id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new MemberNotFoundException(format("Member with id: %s not found", id)));
    }

    private void validateMemberDetails(Member member) {
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new EmailDuplicateException("Unique Email Violation");
        }
    }
}
