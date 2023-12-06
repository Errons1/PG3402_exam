package eu.voops.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DtoCreateAccount {
    
    @NotBlank
    private String internalId;
    
    @Positive
    private long personalId;

    @NotBlank
    private String password;
    
}
