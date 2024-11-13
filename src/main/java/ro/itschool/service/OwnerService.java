package ro.itschool.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.itschool.entity.Book;
import ro.itschool.entity.Owner;
import ro.itschool.repository.BookRepository;
import ro.itschool.repository.OwnerRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final BookRepository bookRepository;

    public Optional<Owner> findById(Integer ownerId) {
        return ownerRepository.findById(ownerId);
    }

    public void saveOwner(Owner owner) {
        ownerRepository.save(owner);
    }

    public void update(Integer id, String name, Integer age) {
        Optional<Owner> owner=ownerRepository.findById(id);
        if (owner.isPresent()){
//            owner.get().setName(name);
            owner.get().setAge(age);
            ownerRepository.save(owner.get());
        }else throw new RuntimeException("owner not found");
    }

    public void deleteById(Integer id) {
        Optional<Owner> owner=ownerRepository.findById(id);
        if (owner.isPresent()){
            for (Book book:owner.get().getBooks()){
                book.getOwners().remove(owner.get());
                bookRepository.save(book);
            }
            ownerRepository.save(owner.get());
            ownerRepository.delete(owner.get());
        }
    }
}
