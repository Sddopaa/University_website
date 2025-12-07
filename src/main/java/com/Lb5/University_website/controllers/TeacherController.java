package com.Lb5.University_website.controllers;

import com.Lb5.University_website.models.*;
import com.Lb5.University_website.services.TaskService;
import com.Lb5.University_website.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 * Контроллер для обработки запросов преподавателя
 */
@Controller
public class TeacherController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    /**
     * Отображает кабинет преподавателя со списками студентов и заданий
     *
     * @param session Сессия HTTP для проверки авторизации
     * @param model   Модель для передачи данных в представление
     * @return Страница кабинета преподавателя или редирект на главную
     */
    @GetMapping("/fromTeacher")
    public String fromTeacherPage(HttpSession session, Model model) {
        User teacherUser = (User) session.getAttribute("user");

        // Проверка авторизации и роли преподавателя
        if (teacherUser == null || !teacherUser.getRole().equals("teacher")) {
            return "redirect:/";
        }

        // Получение объекта Teacher по имени пользователя
        Teacher teacher = userService.getTeacherByUsername(teacherUser.getUserName());
        if (teacher == null) {
            return "redirect:/";
        }

        // Получение студентов того же факультета и заданий преподавателя
        List<Student> students = userService.getStudentsByFaculty(teacher.getFaculty());
        List<Task> tasks = taskService.getTeacherTasks(teacherUser.getId());

        // Добавление данных в модель
        model.addAttribute("title", "Политехнический Университет - Кабинет преподавателя");
        model.addAttribute("teacher", teacher);
        model.addAttribute("students", students);
        model.addAttribute("tasks", tasks);

        return "fromTeacher";
    }

    /**
     * Создает новое задание для студента
     *
     * @param title       Заголовок задания (3-50 символов)
     * @param description Описание задания (20-200 символов)
     * @param studentId   Идентификатор студента
     * @param session     Сессия HTTP для проверки авторизации преподавателя
     * @return Редирект на страницу преподавателя с параметром результата
     */
    @PostMapping("/teacher/createTask")
    public String createTask(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam Long studentId,
            HttpSession session) {

        User teacherUser = (User) session.getAttribute("user");

        // Проверка авторизации преподавателя
        if (teacherUser == null || !teacherUser.getRole().equals("teacher")) {
            return "redirect:/login";
        }

        // Получение объекта Teacher
        Teacher teacher = userService.getTeacherByUsername(teacherUser.getUserName());
        if (teacher == null) {
            return "redirect:/fromTeacher?error=teacher_not_found";
        }

        // Получение студента по ID
        User student = userService.getUserById(studentId);
        if (student == null) {
            return "redirect:/fromTeacher?error=student_not_found";
        }

        // Проверка, что студент принадлежит к тому же факультету
        if (student instanceof Student) {
            Student studentEntity = (Student) student;
            if (!teacher.getFaculty().equals(studentEntity.getFaculty())) {
                return "redirect:/fromTeacher?error=faculty_mismatch";
            }
        }

        // Использование предмета из профиля преподавателя
        String subject = teacher.getSubject();

        // Проверка длины заголовка задания
        if (title.length() < 3 || title.length() > 50) {
            return "redirect:/fromTeacher?error=title_length";
        }

        // Проверка длины описания задания
        if (description.length() < 20 || description.length() > 200) {
            return "redirect:/fromTeacher?error=description_length";
        }

        // Создание задания через сервис
        taskService.createTask(title, description, subject, teacherUser.getId(), studentId);

        return "redirect:/fromTeacher?success=true";
    }

    /**
     * Удаляет задание преподавателя
     *
     * @param taskId  Идентификатор задания для удаления
     * @param session Сессия HTTP для проверки авторизации преподавателя
     * @return Редирект на страницу преподавателя
     */
    @PostMapping("/teacher/deleteTask/{taskId}")
    public String deleteTask(@PathVariable Long taskId, HttpSession session) {
        User teacher = (User) session.getAttribute("user");

        // Проверка авторизации преподавателя
        if (teacher == null || !teacher.getRole().equals("teacher")) {
            return "redirect:/login";
        }

        // Удаление задания с проверкой прав
        boolean deleted = taskService.deleteTask(taskId, teacher.getId());

        return "redirect:/fromTeacher?success=deleted";
    }
}