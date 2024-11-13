package ro.itschool.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ro.itschool.entity.Author;
import ro.itschool.service.AuthorService;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @GetMapping("/save/author")
    public String showAuthorForm(Model model) {
        Author author = new Author();
        model.addAttribute("author", author);
        return "save-author";
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping("/save/author")
    public String addAuthor(@ModelAttribute Author author) {
        authorService.saveAuthor(author);
        return "redirect:/librarian";
    }

    @GetMapping("/find/id/author")
    public String findBookById(@RequestParam("authorId") Integer id, Model model) {
        Optional<Author> optionalAuthor = authorService.findById(id);
        optionalAuthor.ifPresent(author -> model.addAttribute("author", author));
        return "delete-author";
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping("/delete/author/{id}")
    public String deleteAuthorById(@PathVariable("id") Integer id) {
        authorService.deleteById(id);
        return "redirect:/librarian";
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @GetMapping("/delete/author")
    public String deleteAuthor(Model model) {
        return "delete-author";
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @GetMapping("/update/an/author")
    public String updateAnAuthor(){
        return "update-author";
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping("/update/author")
    public String updateBook(@RequestParam Integer id,
                             @RequestParam String name,
                             @RequestParam LocalDate birthDate,
                             @RequestParam(required = false) LocalDate deathDate){
        authorService.updateAuthor(id, name,birthDate,deathDate);
        return "redirect:/librarian";
    }


}
