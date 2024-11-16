package ro.itschool.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ro.itschool.entity.Book;
import ro.itschool.entity.Owner;
import ro.itschool.entity.Role;
import ro.itschool.service.OwnerService;

import java.util.Optional;

@Controller
@RequestMapping("/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/save")
    public String showOwnerForm(Model model) {
        Owner owner = new Owner();
        model.addAttribute("owner", owner);
        return "save-customer";
    }

    @PostMapping("/save")
    public String addOwner(@ModelAttribute Owner owner) {
        String encodedPassword = passwordEncoder.encode(owner.getPassword());
        owner.setPassword(encodedPassword);
        owner.setRole(Role.OWNER);
        ownerService.saveOwner(owner);
        return "redirect:/home";
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("/my-profile")
    public String profile(Model model){
        Owner user = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = user.getId();
        Optional<Owner> owner = ownerService.findById(userId);
        owner.ifPresent(value -> model.addAttribute("owner", value));
        return "profile";
    }

}
