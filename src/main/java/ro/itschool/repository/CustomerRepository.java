package ro.itschool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.itschool.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
