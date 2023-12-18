package eu.voops.customer.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    @NonNull
    @NotBlank
    @Column(name = "internal_id")
    private String internalId;

    @NonNull
    @NotBlank
    @Column(name = "personal_id")
    private String personalId;

    @NonNull
    @NotBlank
    @Column(name = "first_name")
    private String firstName;
    
    @NonNull
    @NotBlank
    @Column(name = "last_name")
    private String lastName;

    @NonNull
    @NotBlank
    @Column(name = "address")
    private String address;

    @NonNull
    @NotBlank
    @Column(name = "tlf")
    private String tlf;

    @NonNull
    @NotBlank
    @Email
    @Column(name = "email")
    private String email;
    
}