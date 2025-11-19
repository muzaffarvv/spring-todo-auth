package uz.pdp.todo.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.todo.dto.UserRegisterDTO;
import uz.pdp.todo.enums.Role;
import uz.pdp.todo.service.AuthUserService;

import java.security.Principal;

@Controller
@RequestMapping("/auth")
public class AuthUserController {
    private final AuthUserService authUserService;

    public AuthUserController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error) {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("error", error);
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserRegisterDTO dto,
                           BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }
        authUserService.register(dto);
        return "redirect:/auth/login";
    }

    @GetMapping("/profile")
    public String profilePage(Model model, Principal principal) {
        authUserService.getByUsername(principal.getName())
                .ifPresent(user -> model.addAttribute("user", user));
        return "profile";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("users", authUserService.getAll());
        return "admin";
    }

    private static final String REDIRECT_ADMIN = "redirect:/auth/admin";

    @PutMapping("/admin/{id}/toggle-block")
    public String toggleBlockStatus(@PathVariable Long id) {
        authUserService.toggleBlockStatus(id);
        return REDIRECT_ADMIN;
    }

    @PutMapping("/admin/{id}/change-role")
    public String changeUserRole(@PathVariable Long id,
                                 @RequestParam Role newRole) {
        authUserService.changeRole(id, newRole);
        return REDIRECT_ADMIN;
    }
}
