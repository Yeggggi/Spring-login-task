package com.example.login_spring.controller;

import com.example.login_spring.domain.User;
import com.example.login_spring.repository.UserRepository;
import com.example.login_spring.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;

    public AuthController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        if (!userRepository.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("loginError", "등록되지 않은 이메일입니다.");
            return "redirect:/login";
        }

        User user = userService.authenticate(email, password);
        if (user == null) {
            redirectAttributes.addFlashAttribute("loginError", "비밀번호가 올바르지 않습니다.");
            redirectAttributes.addFlashAttribute("loginEmail", email);
            return "redirect:/login";
        }

        session.setAttribute("LOGIN_USER_EMAIL", user.getEmail());
        return "redirect:/";
    }

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        String email = (String) session.getAttribute("LOGIN_USER_EMAIL");
        model.addAttribute("email", email);
        return "home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
