package eu.voops.frontend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DtoCreateProfileAuthentication {

    @NotBlank
    private String internalId;

    @NotBlank
    private String personalId;

    @NotBlank
    private String password;
    
}
