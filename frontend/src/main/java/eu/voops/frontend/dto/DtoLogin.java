package eu.voops.frontend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DtoLogin {

    @NotBlank
    private String personalId;

    @NotBlank
    private String password;
    
}
