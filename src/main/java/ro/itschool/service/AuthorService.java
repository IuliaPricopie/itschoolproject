package ro.itschool.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.itschool.entity.Author;
import ro.itschool.entity.Book;
import ro.itschool.repository.AuthorRepository;
import ro.itschool.repository.BookRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;

//    public Author saveAuthor(Author author) {
//        return authorRepository.save(author);
//    }

    public Author saveAuthor(Author author) {
        author.setName(author.getName().toUpperCase());
        return authorRepository.save(author);
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Optional<Author> findById(Integer id) {
        return authorRepository.findById(id);
    }

    public void deleteById(Integer id) {
        Optional<Author> author=authorRepository.findById(id);
        if (author.isPresent()) {
            if (author.get().getBooks()!=null) {
                for (Book book : author.get().getBooks()) {
                    book.setAuthor(null);
                    bookRepository.save(book);
                    bookService.deleteById(book.getId());
                }
            }
            authorRepository.deleteById(id);
        }
    }

    public Optional<Author> findByName(String authorName) {
        return authorRepository.findByName(authorName);
    }

    public void updateAuthor(Integer id, String name, LocalDate birthDate, LocalDate deathDate) {
        Optional<Author> author=authorRepository.findById(id);
        if (author.isPresent()){
            author.get().setName(name.toUpperCase());
            author.get().setBirthDate(birthDate);
            author.get().setDeathDate(deathDate);
            authorRepository.save(author.get());
        }else throw new RuntimeException("Author not found");
    }

    public List<Author> findAll() {
        return authorRepository.findAll();
    }
}
