package eu.voops.frontend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DtoCreateAccount {
    
    @NotBlank
    private String personalId;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String address;

    @NotBlank
    private String tlf;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

}