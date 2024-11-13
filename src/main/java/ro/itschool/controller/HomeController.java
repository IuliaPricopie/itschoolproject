package ro.itschool.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class HomeController {
    @GetMapping("/home")
    public String homePage(){
        return "home";
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @GetMapping("/librarian")
    public String librarianPage(){
        return "librarian";
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("/customer")
    public String customerPage(){
        return "customer";
    }



}
