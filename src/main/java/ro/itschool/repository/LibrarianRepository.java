package ro.itschool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.itschool.entity.Librarian;

import java.util.Optional;

public interface LibrarianRepository extends JpaRepository<Librarian, Integer> {
    Optional<Librarian> findByUsername(String username);
}
