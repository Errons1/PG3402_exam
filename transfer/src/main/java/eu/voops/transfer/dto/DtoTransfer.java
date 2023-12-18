package eu.voops.transfer.dto;

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

    @NotNull
    private Long transferFromBalance;
    
    @NotBlank
    private String transferTo;

    @NotNull
    private Long transferToBalance;
    
}
