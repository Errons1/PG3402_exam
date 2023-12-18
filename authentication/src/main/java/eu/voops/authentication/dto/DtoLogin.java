package eu.voops.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DtoLogin {

    @NotBlank
    private String personalId;

    @NotBlank
    private String password;
    
}
