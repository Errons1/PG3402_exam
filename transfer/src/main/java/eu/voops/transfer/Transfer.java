package eu.voops.transfer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transfer {

    @NotBlank
    private String transferFrom;

    @NotNull
    private Long transferFromBalance;
    
    @NotBlank
    private String transferTo;

    @NotNull
    private Long transferToBalance;
    
}
