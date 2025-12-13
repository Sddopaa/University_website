package com.Lb5.University_website.controllers;

import com.Lb5.University_website.models.Task;
import com.Lb5.University_website.models.User;
import com.Lb5.University_website.services.TaskService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Controller
public class StudentController {

    @Autowired
    private TaskService taskService;

    /**
     * Отображает страницу со списком заданий студента
     *
     * @param session HTTP-сессия для получения данных пользователя
     * @param model Модель для передачи данных в представление
     * @return Имя представления или редирект
     */
    @GetMapping("/tasks")
    public String tasksPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals("student")) return "redirect:/";

        List<Task> tasks = taskService.getStudentTasks(user.getId());
        model.addAttribute("title", "Политехнический Университет - Задания студента");
        model.addAttribute("tasks", tasks);
        return "tasks";
    }

    /**
     * Скачивание файла, связанного с заданием
     *
     * @param fileName Имя файла для скачивания
     * @param session HTTP-сессия для проверки авторизации
     * @return Ответ с файлом или код ошибки
     * @throws Exception если произошла ошибка при работе с файлом
     */
    @GetMapping("/tasks/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals("student")) return ResponseEntity.status(403).build();

        Resource resource = taskService.getResource(fileName);
        if (!resource.exists() || !resource.isReadable()) return ResponseEntity.notFound().build();

        // Определение MIME-типа файла
        Path path = resource.getFile().toPath();
        String contentType = Files.probeContentType(path);
        if (contentType == null) contentType = "application/octet-stream";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);
    }
}