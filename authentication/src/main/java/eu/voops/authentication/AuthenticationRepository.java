package eu.voops.authentication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationRepository extends JpaRepository<Authentication, Long> {
    
    boolean existsByInternalId(String internalId);
    
    Authentication findByInternalIdLike(String internalId);

    void deleteByInternalId(String internalId);
}
