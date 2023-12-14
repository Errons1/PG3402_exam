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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    
    @NotBlank
    @Column(name = "internal_id", nullable = false)
    private String internalId;
    
    @NotBlank
    @Column(name = "personal_id", nullable = false)
    private String personalId;
    
    @NotNull
    @Column(name = "password_hash", nullable = false)
    private byte[] passwordHash;

    public Authentication(String internalId, String personalId, @NotNull byte[] passwordHash) {
        this.internalId = internalId;
        this.personalId = personalId;
        this.passwordHash = passwordHash;
    }
}
