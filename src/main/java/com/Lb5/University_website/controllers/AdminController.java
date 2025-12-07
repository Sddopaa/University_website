package com.Lb5.University_website.controllers;

import com.Lb5.University_website.models.*;
import com.Lb5.University_website.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/adminPanel")
    public String adminPanel(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals("admin")) {
            return "redirect:/";
        }

        List<User> usersForSelection = new ArrayList<>();
        usersForSelection.addAll(studentRepository.findAll());
        usersForSelection.addAll(teacherRepository.findAll());

        model.addAttribute("users", usersForSelection);
        model.addAttribute("title", "Политехнический Университет - Панель админа");
        return "adminPanel";
    }

    @PostMapping("/adminPanel/changeRole")
    public String changeUserRole(
            @RequestParam Long userId,
            @RequestParam String newRole,
            @RequestParam(required = false) Byte course,
            @RequestParam(required = false) Boolean isTuitionFree,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Integer workExperience,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User adminUser = (User) session.getAttribute("user");
        if (adminUser == null || !adminUser.getRole().equals("admin")) {
            return "redirect:/";
        }

        try {
            User oldUser = userRepository.findById(userId).get();

            String userName = oldUser.getUserName();
            String password = oldUser.getPassword();
            String firstName = oldUser.getFirstName();
            String lastName = oldUser.getLastName();
            String patronymic = oldUser.getPatronymic();
            int age = oldUser.getAge();
            String avatarPath = oldUser.getAvatarPath();
            String oldRole = oldUser.getRole();

            String faculty = "";
            if (oldRole.equals("student")) {
                faculty = studentRepository.findById(userId).get().getFaculty();
            } else if (oldRole.equals("teacher")) {
                faculty = teacherRepository.findById(userId).get().getFaculty();
            }

            if (oldRole.equals("student")) {
                studentRepository.deleteById(userId);
            } else if (oldRole.equals("teacher")) {
                teacherRepository.deleteById(userId);
            }
            userRepository.deleteById(userId);

            if (newRole.equals("student")) {
                Student student = new Student(
                        userName, password, firstName, lastName,
                        patronymic, age,
                        course,
                        isTuitionFree,
                        faculty,
                        specialization
                );
                student.setAvatarPath(avatarPath);
                studentRepository.save(student);

            } else if (newRole.equals("teacher")) {
                Teacher teacher = new Teacher(
                        userName, password, firstName, lastName,
                        patronymic, age,
                        subject,
                        department,
                        faculty,
                        workExperience
                );
                teacher.setAvatarPath(avatarPath);
                teacherRepository.save(teacher);
            }

            redirectAttributes.addFlashAttribute("success", "Роль " + userName + " изменена");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
        }

        return "redirect:/adminPanel";
    }

    // Новый endpoint для получения факультета пользователя
    @GetMapping("/adminPanel/getFaculty/{userId}")
    @ResponseBody
    public String getUserFaculty(@PathVariable Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return "";
        }

        User user = userOpt.get();
        if (user.getRole().equals("student")) {
            Optional<Student> studentOpt = studentRepository.findById(userId);
            return studentOpt.map(Student::getFaculty).orElse("");
        } else if (user.getRole().equals("teacher")) {
            Optional<Teacher> teacherOpt = teacherRepository.findById(userId);
            return teacherOpt.map(Teacher::getFaculty).orElse("");
        }
        return "";
    }
}