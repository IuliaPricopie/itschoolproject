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

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("/name/book")
    public String findBookByTitleWithHTML(Model model) {
        return "by-title";
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("/find/name/book")
    public String findBookByName(@RequestParam("bookName") String name, Model model) {
        Optional<Book> optionalBook = bookService.findByName(name.toLowerCase());
        if (optionalBook.isPresent()) {
            model.addAttribute("book", optionalBook.get());
            return "by-title";
        } else return "book-not-found";
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PostMapping("/find/book/{name}")
    public String borrowBookByName(@PathVariable("name") String name, @RequestParam Integer ownerId, Model model) {
        Owner user = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = user.getId();
        Optional<Book> optionalBook = bookService.findByName(name.toLowerCase());
        Optional<Owner> owner = ownerService.findById(ownerId);
        if (optionalBook.isPresent()&&owner.isPresent()&&owner.get().getBorrowedBook()==null&& !optionalBook.get().isBorrowed()){
            bookService.borrow(optionalBook.get(),owner.get());
            return "redirect:/customer";
        } else if (owner.get().getBorrowedBook()!=null) {
            return "library-rules";
        }else {
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
        Optional<Author> author=authorService.findByName(authorName.toUpperCase());
        if (author.isPresent()) {
            List<Book> bookList = bookService.findByAuthorName(authorName.toUpperCase());
            model.addAttribute("books", bookList);
            return "by-author";
        } else return "book-not-found";
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
                                              @RequestParam int price){
        bookService.updateBook(id, name,page,price);
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
                bookService.addToList(book.get(), owner.get());
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

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @GetMapping("/book/for/delete")
    public String getAllBooks(final Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "books-delete";
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping("/book/for/delete/{id}")
    public String deleteBook(@PathVariable Integer id) {
        bookService.deleteById(id);
        return "redirect:/book/for/delete";
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @GetMapping("/return/book")
    public String returnBook(){
        return "return-book";
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping("/return/a/book")
    public String returnBook(@RequestParam Integer id){
        Optional<Book> book=bookService.findById(id);
        if (book.isPresent()&&book.get().getBorrower()!=null){
        bookService.returnBook(id);
        return "redirect:/librarian";}
        else return "unborrowed-book";
    }

}
