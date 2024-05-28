package app.mongodb.kitchensink.controller;

import app.mongodb.kitchensink.model.Member;
import app.mongodb.kitchensink.service.MemberRegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/members")
public class MemberRestController {

    private final MemberRegistrationService memberRegistrationService;

    private MemberRestController(MemberRegistrationService memberRegistrationService) {
        this.memberRegistrationService = memberRegistrationService;
    }

    @GetMapping("/")
    public List<Member> listAllMembers() {
        return memberRegistrationService.getAllMembers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> lookupMemberById(@PathVariable Long id) {
        Member member = memberRegistrationService.getMemberById(id);
        return ResponseEntity.ok(member);
    }

	@PostMapping("/")
	public ResponseEntity<Void> createMember(@Valid @RequestBody Member member) {
        memberRegistrationService.register_member(member);
        return ResponseEntity.ok().build();
	}

}
