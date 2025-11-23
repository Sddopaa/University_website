package com.Lb5.University_website.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Политехнический Университет - Главная страница");
        return "home";
    }

    @GetMapping("/info")
    public String info(Model model) {
        model.addAttribute("title", "Политехнический Университет - Об университете");
        return "info";
    }

    @GetMapping("/schedule")
    public String schedule(Model model) {
        model.addAttribute("title", "Политехнический Университет - Расписание");
        return "schedule";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Политехнический Университет - Вход");
        return "login";
    }
}