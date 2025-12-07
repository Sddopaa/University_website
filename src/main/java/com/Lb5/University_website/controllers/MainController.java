package com.Lb5.University_website.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Основной контроллер для обработки запросов навигации по сайту
 */
@Controller
public class MainController {

    /**
     * Отображает главную страницу сайта
     * @param model Модель для передачи данных в представление
     * @return Имя шаблона главной страницы
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Политехнический Университет - Главная страница");
        return "home";
    }

    /**
     * Отображает страницу с информацией об университете
     * @param model Модель для передачи данных в представление
     * @return Имя шаблона страницы информации
     */
    @GetMapping("/info")
    public String info(Model model) {
        model.addAttribute("title", "Политехнический Университет - Об университете");
        return "info";
    }

    /**
     * Отображает страницу с расписанием занятий
     * @param model Модель для передачи данных в представление
     * @return Имя шаблона страницы расписания
     */
    @GetMapping("/schedule")
    public String schedule(Model model) {
        model.addAttribute("title", "Политехнический Университет - Расписание");
        return "schedule";
    }

    /**
     * Отображает страницу входа в систему
     * @param model Модель для передачи данных в представление
     * @return Имя шаблона страницы входа
     */
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Политехнический Университет - Вход");
        return "login";
    }
}