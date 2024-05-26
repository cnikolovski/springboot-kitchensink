package app.mongodb.kitchensink.controller;

import app.mongodb.kitchensink.model.Member;
import app.mongodb.kitchensink.service.MemberRegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class ModelViewController {

    private static final String MEMBER_ATTRIBUTE = "member";
    private static final String MEMBERS_ATTRIBUTE = "members";
    private static final String RESPONSE_ATTRIBUTE = "responseMessage";

    private final MemberRegistrationService memberRegistrationService;

    @Autowired
    private ModelViewController(MemberRegistrationService memberRegistrationService) {
        this.memberRegistrationService = memberRegistrationService;
    }

    @GetMapping("/")
    public String index(Model model) {
        populateModelAttributes(model, new Member());
        return "index";
    }

    @PostMapping("/member")
    public String register(@Valid Member member, BindingResult result, Model model) {
        if (result.hasErrors()) {
            populateModelAttributes(model, member);
            return "/index";
        }
        return registerNewMember(model, member);
    }

    private String registerNewMember(Model model, Member member) {
        try {
            memberRegistrationService.register_member(member);
            return "redirect:/";
        } catch (Exception exception) {
            model.addAttribute(RESPONSE_ATTRIBUTE, exception.getMessage());
            populateModelAttributes(model, member);
            return "/index";
        }
    }

    private void populateModelAttributes(Model model, Member member) {
        model.addAttribute(MEMBER_ATTRIBUTE, member);
        model.addAttribute(MEMBERS_ATTRIBUTE, memberRegistrationService.getAllMembers());
    }
}
