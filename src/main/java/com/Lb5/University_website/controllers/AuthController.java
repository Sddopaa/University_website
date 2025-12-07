package com.Lb5.University_website.controllers;

import com.Lb5.University_website.models.User;
import com.Lb5.University_website.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контроллер для обработки запросов аутентификации и авторизации пользователей
 */
@Controller
public class AuthController {

    private final UserService userService;

    /**
     * Конструктор с внедрением зависимости UserService
     * @param userService Сервис для работы с пользователями
     */
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Отображает страницу регистрации
     * @param model Модель для передачи данных в представление
     * @return Имя шаблона страницы регистрации
     */
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("title", "Политехнический Университет - Регистрация");
        return "register";
    }

    /**
     * Обрабатывает запрос на регистрацию нового пользователя
     *
     * @param username              Логин пользователя
     * @param password              Пароль пользователя
     * @param role                  Роль пользователя (student/teacher)
     * @param lastName              Фамилия пользователя
     * @param firstName             Имя пользователя
     * @param patronymic            Отчество пользователя (опционально)
     * @param age                   Возраст пользователя
     * @param course                Курс обучения (только для студентов)
     * @param studentFaculty        Факультет студента (только для студентов)
     * @param specialization        Специализация (только для студентов)
     * @param isTuitionFree         Признак бесплатного обучения (только для студентов)
     * @param teacherFaculty        Факультет преподавателя (только для преподавателей)
     * @param subject               Преподаваемый предмет (только для преподавателей)
     * @param department            Кафедра (только для преподавателей)
     * @param workExperience        Опыт работы (только для преподавателей)
     * @param session               Сессия HTTP для сохранения данных пользователя
     * @param model                 Модель для передачи данных в представление
     * @return Редирект на профиль при успехе или страницу регистрации с ошибкой
     */
    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String role,
                               @RequestParam String lastName,
                               @RequestParam String firstName,
                               @RequestParam(required = false) String patronymic,
                               @RequestParam int age,
                               // Дополнительные поля для Student
                               @RequestParam(required = false) Byte course,
                               @RequestParam(required = false) String studentFaculty,
                               @RequestParam(required = false) String specialization,
                               @RequestParam(required = false) Boolean isTuitionFree,
                               // Дополнительные поля для Teacher
                               @RequestParam(required = false) String teacherFaculty,
                               @RequestParam(required = false) String subject,
                               @RequestParam(required = false) String department,
                               @RequestParam(required = false) Integer workExperience,
                               HttpSession session,
                               Model model) {

        try {
            // Определение факультета в зависимости от роли
            String faculty = null;
            if ("student".equals(role)) {
                faculty = studentFaculty;
            } else if ("teacher".equals(role)) {
                faculty = teacherFaculty;
            }

            // Регистрация пользователя через сервис
            User user = userService.registerUser(
                    username, password, role, firstName, lastName, patronymic, age,
                    course, isTuitionFree, faculty, specialization,
                    subject, department, workExperience
            );

            // Сохранение пользователя в сессии
            session.setAttribute("user", user);
            return "redirect:/profile";

        } catch (Exception e) {
            // Обработка ошибок регистрации
            model.addAttribute("error", e.getMessage());
            model.addAttribute("title", "Политехнический Университет - Регистрация");
            return "register";
        }
    }

    /**
     * Отображает страницу профиля пользователя
     *
     * @param session   Сессия HTTP для получения данных пользователя
     * @param model     Модель для передачи данных в представление
     * @param response  HTTP ответ для настройки заголовков
     * @return Страница профиля или редирект на страницу входа
     */
    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model, HttpServletResponse response) {
        User user = (User) session.getAttribute("user");

        // Проверка авторизации пользователя
        if (user == null) {
            return "redirect:/login";
        }

        // Получение актуальных данных пользователя из базы данных
        User currentUser = userService.getUserByUsername(user.getUserName());
        model.addAttribute("user", currentUser);
        model.addAttribute("title", "Политехнический Университет - Профиль");

        return "profile";
    }

    /**
     * Обрабатывает выход пользователя из системы
     *
     * @param session Сессия HTTP для очистки данных пользователя
     * @return Редирект на главную страницу
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    /**
     * Обрабатывает запрос на вход пользователя в систему
     *
     * @param username  Логин пользователя
     * @param password  Пароль пользователя
     * @param session   Сессия HTTP для сохранения данных пользователя
     * @param model     Модель для передачи данных в представление
     * @return Редирект на профиль при успешном входе или страницу входа с ошибкой
     */
    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {

        User user = userService.loginUser(username, password);

        if (user != null) {
            // Успешный вход - сохранение в сессии
            session.setAttribute("user", user);
            return "redirect:/profile";
        } else {
            // Неверные учетные данные
            model.addAttribute("error", "Неверный логин или пароль");
            model.addAttribute("title", "Политехнический Университет - Вход");
            return "login";
        }
    }
}