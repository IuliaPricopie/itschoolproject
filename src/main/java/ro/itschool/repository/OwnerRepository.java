package ro.itschool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.itschool.entity.Owner;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Integer> {
    Optional<Owner> findByUsername(String username);
}
