package com.Lb5.University_website.controllers;

import com.Lb5.University_website.models.User;
import com.Lb5.University_website.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("title", "Политехнический Университет - Регистрация");
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String role,
                               @RequestParam String lastName,
                               @RequestParam String firstName,
                               @RequestParam(required = false) String patronymic,
                               @RequestParam int age,
                               Model model) {

        try {
            User user = userService.registerUser(username, password, role,
                    firstName, lastName, patronymic, age);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("title", "Политехнический Университет - Регистрация");
            return "register";
        }
    }
}