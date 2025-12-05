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

@Controller
public class TeacherController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @GetMapping("/fromTeacher")
    public String fromTeacherPage(HttpSession session, Model model) {
        User teacherUser = (User) session.getAttribute("user");
        if (teacherUser == null || !teacherUser.getRole().equals("teacher")) {
            return "redirect:/";
        }

        Teacher teacher = userService.getTeacherByUsername(teacherUser.getUserName());
        if (teacher == null) {
            return "redirect:/";
        }

        List<Student> students = userService.getStudentsByFaculty(teacher.getFaculty());
        List<Task> tasks = taskService.getTeacherTasks(teacherUser.getId());

        model.addAttribute("title", "Политехнический Университет - Кабинет преподавателя");
        model.addAttribute("teacher", teacher);
        model.addAttribute("students", students);
        model.addAttribute("tasks", tasks);

        return "fromTeacher";
    }

    @PostMapping("/teacher/createTask")
    public String createTask(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam Long studentId,
            HttpSession session) {

        User teacherUser = (User) session.getAttribute("user");
        if (teacherUser == null || !teacherUser.getRole().equals("teacher")) {
            return "redirect:/login";
        }

        Teacher teacher = userService.getTeacherByUsername(teacherUser.getUserName());
        if (teacher == null) {
            return "redirect:/fromTeacher?error=teacher_not_found";
        }

        User student = userService.getUserById(studentId);
        if (student == null) {
            return "redirect:/fromTeacher?error=student_not_found";
        }

        if (student instanceof Student) {
            Student studentEntity = (Student) student;
            if (!teacher.getFaculty().equals(studentEntity.getFaculty())) {
                return "redirect:/fromTeacher?error=faculty_mismatch";
            }
        }

        // Берем subject из профиля учителя
        String subject = teacher.getSubject();

        // Проверка длины title (добавляем проверку на сервере тоже)
        if (title.length() < 3 || title.length() > 50) {
            return "redirect:/fromTeacher?error=title_length";
        }

        // Проверка длины description
        if (description.length() < 20 || description.length() > 200) {
            return "redirect:/fromTeacher?error=description_length";
        }

        taskService.createTask(title, description, subject, teacherUser.getId(), studentId);

        return "redirect:/fromTeacher?success=true";
    }

    @PostMapping("/teacher/deleteTask/{taskId}")
    public String deleteTask(@PathVariable Long taskId, HttpSession session) {
        User teacher = (User) session.getAttribute("user");
        if (teacher == null || !teacher.getRole().equals("teacher")) {
            return "redirect:/login";
        }

        boolean deleted = taskService.deleteTask(taskId, teacher.getId());
        return "redirect:/fromTeacher?success=deleted";
    }
}