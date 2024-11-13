package ro.itschool.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.itschool.entity.Author;
import ro.itschool.entity.Book;
import ro.itschool.entity.Owner;
import ro.itschool.repository.AuthorRepository;
import ro.itschool.repository.BookRepository;
import ro.itschool.repository.OwnerRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final OwnerRepository ownerRepository;

    public Book saveBook(Book book){
            Author author=book.getAuthor();
            if (book.getAuthor().getId()!=null){ //SA POT ATRIBUI O CARTE UNUI AUTOR
                Optional<Author> optionalAuthor=authorRepository.findById(book.getAuthor().getId());
                if (optionalAuthor.isEmpty()){
                    throw new RuntimeException("Author is null");
                }
                Author authorToBeAdded =optionalAuthor.get();
                book.setAuthor(authorToBeAdded);
            } else { //SALVEAZA UN AUTOR NOU
                Author newAuthor=authorRepository.save(author);
                book.setAuthor(newAuthor);
//                throw new RuntimeException("The book must have an author!");
            }
        book.setName(book.getName().toUpperCase());
        return bookRepository.save(book);
    }


    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Integer id) {
        return bookRepository.findById(id);
    }

    public void deleteById(Integer id) {
        bookRepository.deleteById(id);
    }

    public Optional<Book> findByName(String name) {
        return bookRepository.findByName(name);
    }

    public List<Book> findByAuthorName(String name) {
        return bookRepository.findByAuthorName(name);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public void updateBook(Integer id, String name, int page, int price, Boolean isBorrowed) {
        Optional<Book> book=bookRepository.findById(id);
        if (book.isPresent()){
            book.get().setName(name);
            book.get().setPage(page);
            book.get().setPrice(price);
            book.get().setBorrowed(isBorrowed);
            bookRepository.save(book.get());
        }else throw new RuntimeException("book not found");
    }

    public void addToList(Book book, Owner owner) {
        owner.getBooks().add(book);
        book.getOwners().add(owner);
        bookRepository.save(book);
        ownerRepository.save(owner);
    }

    public void borrow(Book book, Owner owner) {
        book.setBorrowed(true);
        book.setBorrower(owner);
        owner.setBorrowedBook(book);
        bookRepository.save(book);
        ownerRepository.save(owner);
    }
}
