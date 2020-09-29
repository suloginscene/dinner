package me.scene.dinner.domain.account;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Getter @Setter
public class SignupForm {

    @NotBlank @Length(min = 2, max = 16)
    @Pattern(regexp = "^[가-힣a-zA-Z0-9\\-_.]{2,16}$")
    private String username;

    @NotBlank @Email
    private String email;

    @NotBlank @Length(min = 8, max = 50)
    private String password;

    @NotNull @AssertTrue
    private Boolean agreement;

}
