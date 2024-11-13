package ro.itschool.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ro.itschool.entity.Author;
import ro.itschool.entity.Book;
import ro.itschool.entity.Owner;
import ro.itschool.service.AuthorService;
import ro.itschool.service.BookService;
import ro.itschool.service.OwnerService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final OwnerService ownerService;

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @GetMapping("/save/book")
    public String showBookForm(Model model) {
        Book book = new Book();
        model.addAttribute("book", book);
        return "save-book";
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping("/save/book")
    public String addBook(@ModelAttribute Book book) {
        bookService.saveBook(book);
        return "redirect:/librarian";
    }



    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @GetMapping("/delete/book")
    public String deleteBookWithHTML(Model model) {
        return "delete-book";
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @GetMapping("/find/id/book")
    public String findBookById(@RequestParam("bookId") Integer id, Model model) {
        Optional<Book> optionalBook = bookService.findById(id);
        optionalBook.ifPresent(book -> model.addAttribute("book", book));
        return "delete-book";
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping("/delete/book/{id}")
    public String deleteBookById(@PathVariable("id") Integer id) {
        bookService.deleteById(id);
        return "redirect:/librarian";
    }


    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("/name/book")
    public String findBookByTitleWithHTML(Model model) {
        return "by-title";
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("/find/name/book")
    public String findBookByName(@RequestParam("bookName") String name, Model model) {
        Optional<Book> optionalBook = bookService.findByName(name.toLowerCase());
        optionalBook.ifPresent(book -> model.addAttribute("book", book));
        return "by-title";
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PostMapping("/find/book/{name}")
    public String borrowBookByName(@PathVariable("name") String name, @RequestParam Integer ownerId, Model model) {
        Owner user = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = user.getId();
        Optional<Book> optionalBook = bookService.findByName(name.toLowerCase());
        Optional<Owner> owner = ownerService.findById(ownerId);
        if (optionalBook.isPresent()&&owner.isPresent()&& !optionalBook.get().isBorrowed()){
            Book book=optionalBook.get();
            book.setBorrowed(true);
            book.setBorrower(owner.get());
            owner.get().setBorrowedBook(book);
            bookService.saveBook(book);
            ownerService.saveOwner(owner.get());
            return "redirect:/customer";
        } else {
            throw new RuntimeException("This book cannot be borrowed");
        }
    }


    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("/author/book")
    public String findBookByAuthorWithHTML(Model model) {
        return "by-author";
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("/find/author/book")
    public String findBooksByAuthor(@RequestParam("authorName") String authorName, Model model) {
        List<Book> bookList = bookService.findByAuthorName(authorName.toUpperCase());
        model.addAttribute("books", bookList);
        return "by-author";
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PostMapping("/find/book/author/{authorName}")
    public String borrowBookByAuthor(@PathVariable("authorName") String authorName, Model model) {
        Optional<Author> author=authorService.findByName(authorName);
        if (author.isPresent()){
            for (Book book:author.get().getBooks()){
                if (!book.isBorrowed()){
                    book.setBorrowed(true);
                    bookService.saveBook(book);
                    return "redirect:/customer";
                } else {
                    throw new RuntimeException("This book cannot be borrowed");
                }
            }
        }else throw new RuntimeException("Author not found");
        return "redirect:/customer";
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("/all/books")
    public String allBooks(Model model){
        model.addAttribute("books", bookService.findAll());
        return "all-books";
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @GetMapping("/update/a/book")
    public String updateABook(){
        return "update-book";
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping("/update/book")
    public String updateBook(@RequestParam Integer id,
                                              @RequestParam String name,
                                              @RequestParam int page,
                                              @RequestParam int price,
                                              @RequestParam Boolean isBorrowed){
        bookService.updateBook(id, name,page,price,isBorrowed);
        return "redirect:/librarian";
    }

        @PreAuthorize("hasRole('ROLE_OWNER')")
        @PostMapping("/addToList")
        public String addToList(@RequestParam Integer bookId, @RequestParam Integer ownerId, Model model) {
            Owner user = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Integer userId = user.getId();
            Optional<Book> book = bookService.findById(bookId);
            Optional<Owner> owner = ownerService.findById(ownerId);
            if (book.isPresent() && owner.isPresent()) {
                owner.get().getBooks().add(book.get());
                ownerService.saveOwner(owner.get());
                book.get().getOwners().add(owner.get());
                bookService.saveBook(book.get());
            }
            return "redirect:/customer";
        }

        @PreAuthorize("hasRole('ROLE_OWNER')")
        @PostMapping("/delete/list")
        public String deleteFromList(@RequestParam Integer bookId, @RequestParam Integer ownerId,Model model) {
            Owner user = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Integer userId = user.getId();
            Optional<Book> optionalBook = bookService.findById(bookId);
            Optional<Owner> owner = ownerService.findById(ownerId);
            if (optionalBook.isPresent() && owner.isPresent()) {
                optionalBook.get().getOwners().remove(owner.get());
                owner.get().getBooks().remove(optionalBook.get());
                bookService.saveBook(optionalBook.get());
                ownerService.saveOwner(owner.get());
            }
//            return "redirect:/owner/my-profile";
            return "redirect:/owner/my-profile";

        }
}






