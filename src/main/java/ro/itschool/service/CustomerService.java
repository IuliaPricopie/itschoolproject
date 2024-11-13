package ro.itschool.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ro.itschool.repository.LibrarianRepository;
import ro.itschool.repository.OwnerRepository;

@Service
@RequiredArgsConstructor
public class CustomerService implements UserDetailsService{
    private final OwnerRepository ownerRepository;
    private final LibrarianRepository librarianRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return ownerRepository.findByUsername(username)
                .map(owner -> (UserDetails) owner)
                .orElseGet(() -> librarianRepository.findByUsername(username)
                        .map(librarian -> (UserDetails) librarian)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found with " +
                                        "username: " + username))
                        );    }
}
