package eu.voops.account.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DtoAccount {

    @NotBlank
    private String internalId;
    
    @NotBlank
    private String accountName;

    @NotBlank
    private String accountNumber;

    @NonNull
    private Long balance;
    
}
