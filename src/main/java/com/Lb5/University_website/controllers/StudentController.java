package com.Lb5.University_website.controllers;

import com.Lb5.University_website.models.User;
import com.Lb5.University_website.models.Task;
import com.Lb5.University_website.services.TaskService;
import com.Lb5.University_website.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 * Контроллер для обработки запросов студента
 */
@Controller
public class StudentController {

    @Autowired
    private TaskService taskService;

    /**
     * Отображает страницу с заданиями студента
     *
     * @param session Сессия HTTP для проверки авторизации и роли
     * @param model   Модель для передачи данных в представление
     * @return Страница с заданиями или редирект на главную страницу
     */
    @GetMapping("/tasks")
    public String tasksPage(HttpSession session, Model model) {
        // Получение пользователя из сессии
        User user = (User) session.getAttribute("user");

        // Проверка авторизации и роли студента
        if (user == null || !user.getRole().equals("student")) {
            return "redirect:/";
        }

        // Получение списка заданий для студента
        List<Task> tasks = taskService.getStudentTasks(user.getId());

        // Добавление данных в модель
        model.addAttribute("title", "Политехнический Университет - Задания студента");
        model.addAttribute("tasks", tasks);

        return "tasks";
    }
}