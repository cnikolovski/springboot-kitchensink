package app.mongodb.kitchensink.model;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "member")
public class Member {

    private static final int MIN_NAME_SIZE = 1;
    private static final int MAX_NAME_SIZE = 25;
    private static final int MIN_PHONE_NUMBER_SIZE = 10;
    private static final int MAX_PHONE_NUMBER_SIZE = 12;
    @Id
    private String id;

    @NotNull
    @Size(min = MIN_NAME_SIZE, max = MAX_NAME_SIZE)
    @Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
    private String name;

    @NotNull
    @NotEmpty
    @Email
    private String email;

    @NotNull
    @Size(min = MIN_PHONE_NUMBER_SIZE, max = MAX_PHONE_NUMBER_SIZE)
    @Digits(fraction = 0, integer = MAX_PHONE_NUMBER_SIZE)
    private String phoneNumber;
}
