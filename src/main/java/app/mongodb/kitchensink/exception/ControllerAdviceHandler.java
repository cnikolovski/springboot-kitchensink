package app.mongodb.kitchensink.exception;

import app.mongodb.kitchensink.service.MemberRegistrationService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@AllArgsConstructor
public class ControllerAdviceHandler {

    private static final String EMAIL_TAKEN_ERROR_MESSAGE = "Email taken";
    private static final String EMAIL_PROPERTY = "email";
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberRegistrationService.class);


    @ExceptionHandler(value = MemberNotFoundException.class)
    public ResponseEntity<String> handleMemberNotFoundException(MemberNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleRequestBodyValidationExceptions(BindException ex) {
        LOGGER.error("Validation completed. violations found: " + ex.getFieldErrors().size());
        Map<String, String> errorMap = ex.getFieldErrors().stream()
                .collect(Collectors.toMap(error -> error.getField(), error -> error.getDefaultMessage()));
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailDuplicateException.class)
    public ResponseEntity<Map<String, String>> handleEmailDuplicateException(EmailDuplicateException ex) {
        Map<String, String> emailMap = new HashMap<>();
        emailMap.put(EMAIL_PROPERTY, EMAIL_TAKEN_ERROR_MESSAGE);
        return new ResponseEntity<>(emailMap, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
