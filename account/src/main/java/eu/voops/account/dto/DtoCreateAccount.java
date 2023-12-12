package eu.voops.account.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DtoCreateAccount {
    
    @NotBlank
    @Column(name = "internal_id")
    private String internalId;

    @NotBlank
    @Column(name = "account_name")
    private String accountName;
    
}
