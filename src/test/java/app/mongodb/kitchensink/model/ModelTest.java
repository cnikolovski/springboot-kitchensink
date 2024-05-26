package app.mongodb.kitchensink.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


public class ModelTest {

    private static final String NAME = "Bob Smith";
    private static final String EMAIL = "bob.smith@test.com";
    private static final String PHONE_NUMBER = "5566890410";

    private static Validator validator;

    @BeforeAll
    public static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void test_has_no_validation_errors() {
        Member member = Member.builder().name(NAME).email(EMAIL).phoneNumber(PHONE_NUMBER).build();
        Set<ConstraintViolation<Member>> constraintViolations = validator.validate(member);
        assertThat(constraintViolations.size()).isZero();
    }

    @Test
    void test_invalid_name_value() {
        Member member = Member.builder().name("123@#$4").email(EMAIL).phoneNumber(PHONE_NUMBER).build();
        List<String> validation_warnings = execute_validation(member);
        assertThat(validation_warnings).isNotEmpty();
        assertThat(validation_warnings.contains("name"));
    }

    @Test
    void test_invalid_email_value() {
        Member member = Member.builder().name(NAME).email("32423").phoneNumber(PHONE_NUMBER).build();
        List<String> validation_warnings = execute_validation(member);
        assertThat(validation_warnings).isNotEmpty();
        assertThat(validation_warnings.contains("email"));
    }

    @Test
    void test_invalid_phoneNumber_value() {
        Member member = Member.builder().name(NAME).email(EMAIL).phoneNumber("45678").build();
        List<String> validation_warnings = execute_validation(member);
        assertThat(validation_warnings).isNotEmpty();
        assertThat(validation_warnings.contains("phoneNumber"));
    }

    private List<String> execute_validation(Member member) {
        return validator.validate(member).stream()
                .map(violation -> violation.getPropertyPath().toString())
                .collect(Collectors.toList());
    }
}
