package eu.voops.authentication;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "authentications")
public class Authentication {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @Column(name = "internal_id", nullable = false)
    @NotBlank
    private String internalId;
    
    @Column(name = "personal_id", nullable = false)
    @NotBlank
    private String personalId;
    
    @Column(name = "password_hash", nullable = false)
    @NotNull
    private byte[] passwordHash;

    public Authentication(String internalId, String personalId, @NotNull byte[] passwordHash) {
        this.internalId = internalId;
        this.personalId = personalId;
        this.passwordHash = passwordHash;
    }
}
