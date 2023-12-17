package eu.voops.transfer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DtoTransferAccountBalance {
    
    @NotNull
    private Long transferFromBalance;

    @NotNull
    private Long transferToBalance;

}
