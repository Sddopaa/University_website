package com.Lb5.University_website.controllers;

import com.Lb5.University_website.models.User;
import com.Lb5.University_website.models.Task;  // ДОБАВЛЯЕМ ЭТОТ ИМПОРТ
import com.Lb5.University_website.services.TaskService;  // ДОБАВЛЯЕМ ЭТОТ ИМПОРТ
import com.Lb5.University_website.services.UserService;  // ДОБАВЛЯЕМ ЭТОТ ИМПОРТ
import org.springframework.beans.factory.annotation.Autowired;  // ДОБАВЛЯЕМ ЭТОТ ИМПОРТ
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import java.util.List;  // ДОБАВЛЯЕМ ЭТОТ ИМПОРТ

@Controller
public class StudentController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/tasks")
    public String tasksPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals("student")) {
            return "redirect:/";
        }

        List<Task> tasks = taskService.getStudentTasks(user.getId());
        model.addAttribute("title", "Политехнический Университет - Задания студента");
        model.addAttribute("tasks", tasks);

        return "tasks";
    }
}