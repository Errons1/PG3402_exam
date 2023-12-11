package eu.voops.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByPersonalId(String personalId);

    Customer findByPersonalId(String personalId);
}
