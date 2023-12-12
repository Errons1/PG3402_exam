package eu.voops.account;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "accounts")
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @NonNull
    @NotBlank
    @Column(name = "internal_id")
    private String internalId;
    
    @NonNull
    @NotBlank
    @Column(name = "account_name")
    private String accountName;
    
    @NonNull
    @NotBlank
    @Column(name = "account_number")
    private String accountNumber;
    
    @NonNull
    @Column(name = "balance")
    private Double balance;
    
}
