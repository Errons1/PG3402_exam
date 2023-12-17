package eu.voops.frontend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DtoTransfer {

    @NotBlank
    private String transferFrom;
    
    @NotBlank
    private String transferTo;
    
    @NotNull
    private Long amount;

}
