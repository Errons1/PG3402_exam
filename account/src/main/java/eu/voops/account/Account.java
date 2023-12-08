package eu.voops.account;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    private long id;
    
    @NotBlank
    @Column(name = "internal_id")
    private String internalId;

    @NotBlank
    @Column(name = "personal_id")
    private String personalId;

    @NotBlank
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Column(name = "last_name")
    private String lastName;

    @NotBlank
    @Column(name = "address")
    private String address;

    @NotBlank
    @Column(name = "tlf")
    private String tlf;

    @NotBlank
    @Email
    @Column(name = "email")
    private String email;
    
}
