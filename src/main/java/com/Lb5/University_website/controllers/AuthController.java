package com.Lb5.University_website.controllers;

import com.Lb5.University_website.models.User;
import com.Lb5.University_website.service.UserService;
import jakarta.servlet.http.HttpSession;
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
                               // Дополнительные поля для Student
                               @RequestParam(required = false) Byte course,
                               @RequestParam(required = false) String faculty,
                               @RequestParam(required = false) String specialization,
                               @RequestParam(required = false) Boolean isTuitionFree,
                               // Дополнительные поля для Teacher
                               @RequestParam(required = false) String subject,
                               @RequestParam(required = false) String department,
                               @RequestParam(required = false) Integer workExperience,
                               HttpSession session,
                               Model model) {

        try {
            User user = userService.registerUser(
                    username, password, role, firstName, lastName, patronymic, age,
                    course, isTuitionFree, faculty, specialization,
                    subject, department, workExperience
            );

            // Сохраняем пользователя в сессию
            session.setAttribute("user", user);
            return "redirect:/profile";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("title", "Политехнический Университет - Регистрация");
            return "register";
        }
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // Загружаем пользователя (будет базовый User)
        User currentUser = userService.getUserByUsername(user.getUserName());
        model.addAttribute("user", currentUser);
        return "profile";
    }
}

