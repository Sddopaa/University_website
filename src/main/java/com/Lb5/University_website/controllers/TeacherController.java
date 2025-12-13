package com.Lb5.University_website.controllers;

import com.Lb5.University_website.models.*;
import com.Lb5.University_website.services.TaskService;
import com.Lb5.University_website.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Controller
public class TeacherController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    /**
     * Отображает страницу кабинета преподавателя
     *
     * @param session HTTP-сессия для получения данных пользователя
     * @param model Модель для передачи данных в представление
     * @return Имя представления или редирект
     */
    @GetMapping("/fromTeacher")
    public String fromTeacherPage(HttpSession session, Model model) {
        User teacherUser = (User) session.getAttribute("user");

        if (teacherUser == null || !teacherUser.getRole().equals("teacher")) return "redirect:/";

        Teacher teacher = userService.getTeacherByUsername(teacherUser.getUserName());
        if (teacher == null) return "redirect:/";

        List<Student> students = userService.getStudentsByFaculty(teacher.getFaculty());
        List<Task> tasks = taskService.getTeacherTasks(teacherUser.getId());

        model.addAttribute("title", "Политехнический Университет - Кабинет преподавателя");
        model.addAttribute("teacher", teacher);
        model.addAttribute("students", students);
        model.addAttribute("tasks", tasks);

        return "fromTeacher";
    }

    /**
     * Создает новое задание для студента
     *
     * @param title Название задания (3-50 символов)
     * @param description Описание задания (20-200 символов)
     * @param studentId Идентификатор студента
     * @param files Прикрепляемые файлы (опционально)
     * @param session HTTP-сессия для проверки авторизации
     * @return Перенаправление на страницу преподавателя
     * @throws IOException если произошла ошибка при сохранении файлов
     */
    @PostMapping("/teacher/createTask")
    public String createTask(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam Long studentId,
            @RequestParam(required = false) MultipartFile[] files,
            HttpSession session) throws IOException {

        User teacherUser = (User) session.getAttribute("user");
        if (teacherUser == null || !teacherUser.getRole().equals("teacher")) return "redirect:/login";

        Teacher teacher = userService.getTeacherByUsername(teacherUser.getUserName());
        if (teacher == null) return "redirect:/fromTeacher?error=teacher_not_found";

        User student = userService.getUserById(studentId);
        if (student == null) return "redirect:/fromTeacher?error=student_not_found";

        // Проверка, что студент из того же факультета
        if (student instanceof Student) {
            Student studentEntity = (Student) student;
            if (!teacher.getFaculty().equals(studentEntity.getFaculty())) {
                return "redirect:/fromTeacher?error=faculty_mismatch";
            }
        }

        if (title.length() < 3 || title.length() > 50) return "redirect:/fromTeacher?error=title_length";
        if (description.length() < 20 || description.length() > 200) return "redirect:/fromTeacher?error=description_length";

        // Создание задания
        Task task = taskService.createTask(title, description, teacher.getSubject(), teacherUser.getId(), studentId);

        // Сохранение прикрепленных файлов
        if (files != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String savedFileName = taskService.saveFile(file, task.getId());
                    task.addAttachmentPath(savedFileName);
                }
            }
            taskService.saveTask(task);
        }

        return "redirect:/fromTeacher?success=true";
    }

    /**
     * Удаляет задание преподавателя
     *
     * @param taskId Идентификатор задания для удаления
     * @param session HTTP-сессия для проверки авторизации
     * @return Перенаправление на страницу преподавателя
     */
    @PostMapping("/teacher/deleteTask/{taskId}")
    public String deleteTask(@PathVariable Long taskId, HttpSession session) {
        User teacher = (User) session.getAttribute("user");
        if (teacher == null || !teacher.getRole().equals("teacher")) return "redirect:/login";

        taskService.deleteTask(taskId, teacher.getId());
        return "redirect:/fromTeacher?success=deleted";
    }

    /**
     * Скачивание файла, связанного с заданием
     *
     * @param fileName Имя файла для скачивания
     * @param session HTTP-сессия для проверки авторизации
     * @return Ответ с файлом или код ошибки
     * @throws IOException если произошла ошибка при работе с файлом
     */
    @GetMapping("/teacher/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpSession session) throws IOException {
        User teacher = (User) session.getAttribute("user");
        if (teacher == null || !teacher.getRole().equals("teacher")) {
            return ResponseEntity.status(403).build();
        }

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