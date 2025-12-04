package com.Lb5.University_website.controllers;

import com.Lb5.University_website.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
    @GetMapping("/adminPanel")
    public String fromTeacherPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            return "redirect:/";
        }
        model.addAttribute("title", "Политехнический Университет - Панель админа");
        return "adminPanel";
    }
}