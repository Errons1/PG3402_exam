package eu.voops.authentication;

import eu.voops.authentication.entity.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationRepository extends JpaRepository<Authentication, Long> {
    
    boolean existsByInternalId(String internalId);
    
    Authentication findByInternalIdLike(String internalId);

    void deleteByInternalId(String internalId);

    Authentication findByPersonalId(String personalId);

    boolean existsByPersonalId(String personalId);
    
}
