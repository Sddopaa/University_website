// САМЫЙ ПРОСТОЙ ВАРИАНТ UserService.java
package com.Lb5.University_website.services;

import com.Lb5.University_website.models.User;
import com.Lb5.University_website.models.Student;
import com.Lb5.University_website.models.Teacher;
import com.Lb5.University_website.models.Admin;
import com.Lb5.University_website.repository.UserRepository;
import com.Lb5.University_website.repository.StudentRepository;
import com.Lb5.University_website.repository.TeacherRepository;
import com.Lb5.University_website.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для работы с пользователями.
 * Обеспечивает регистрацию, аутентификацию и управление пользователями
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final AdminRepository adminRepository;

    /**
     * Конструктор с внедрением зависимостей репозиториев
     *
     * @param userRepository     Репозиторий для общих пользователей
     * @param studentRepository  Репозиторий для студентов
     * @param teacherRepository  Репозиторий для преподавателей
     * @param adminRepository    Репозиторий для администраторов
     */
    public UserService(UserRepository userRepository,
                       StudentRepository studentRepository,
                       TeacherRepository teacherRepository,
                       AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.adminRepository = adminRepository;
    }

    /**
     * Регистрирует нового пользователя в системе
     *
     * @param userName          Логин пользователя
     * @param password          Пароль пользователя
     * @param role              Роль пользователя (student/teacher/admin)
     * @param firstName         Имя пользователя
     * @param lastName          Фамилия пользователя
     * @param patronymic        Отчество пользователя
     * @param age               Возраст пользователя
     * @param course            Курс обучения (только для студентов)
     * @param isTuitionFree     Признак бесплатного обучения (только для студентов)
     * @param faculty           Факультет (для студентов и преподавателей)
     * @param specialization    Специализация (только для студентов)
     * @param subject           Преподаваемый предмет (только для преподавателей)
     * @param department        Кафедра (только для преподавателей)
     * @param workExperience    Стаж работы (только для преподавателей)
     * @return Зарегистрированный пользователь
     * @throws IllegalArgumentException Если логин уже занят или указана неизвестная роль
     */
    public User registerUser(String userName, String password, String role,
                             String firstName, String lastName, String patronymic, int age,
                             Byte course, Boolean isTuitionFree, String faculty, String specialization,
                             String subject, String department, Integer workExperience) {

        // Проверка уникальности логина
        if (userRepository.existsByUserName(userName)) {
            throw new IllegalArgumentException("Пользователь с именем '" + userName + "' уже занят");
        }

        // Создание пользователя в зависимости от роли
        switch (role) {
            case "student":
                Student student = new Student(userName, password, firstName, lastName,
                        patronymic, age,
                        course != null ? course : 1,
                        isTuitionFree != null ? isTuitionFree : false,
                        faculty != null ? faculty : "Не указан",
                        specialization != null ? specialization : "Не указана");
                return studentRepository.save(student);

            case "teacher":
                Teacher teacher = new Teacher(userName, password, firstName, lastName,
                        patronymic, age,
                        subject != null ? subject : "Не указан",
                        department != null ? department : "Не указана",
                        faculty != null ? faculty : "Не указан",
                        workExperience != null ? workExperience : 0);
                return teacherRepository.save(teacher);

            case "admin":
                Admin admin = new Admin(userName, password, firstName, lastName, patronymic, age);
                return adminRepository.save(admin);

            default:
                throw new IllegalArgumentException("Неизвестная роль: " + role);
        }
    }

    /**
     * Выполняет вход пользователя в систему
     *
     * @param userName Логин пользователя
     * @param password Пароль пользователя
     * @return Авторизованный пользователь или null если данные неверны
     */
    public User loginUser(String userName, String password) {
        return userRepository.findByUserName(userName)
                .filter(user -> user.getPassword().equals(password))
                .orElse(null);
    }

    /**
     * Получает пользователя по имени пользователя
     *
     * @param userName Логин пользователя
     * @return Найденный пользователь или null если не найден
     */
    public User getUserByUsername(String userName) {
        return userRepository.findByUserName(userName).orElse(null);
    }

    /**
     * Получает пользователя по идентификатору
     *
     * @param id Идентификатор пользователя
     * @return Найденный пользователь или null если не найден
     */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Получает полное ФИО пользователя в формате "Фамилия И. О."
     *
     * @param userId Идентификатор пользователя
     * @return Полное ФИО или "Неизвестный пользователь" если пользователь не найден
     */
    public String getUserFullNameById(Long userId) {
        User user = getUserById(userId);
        if (user == null) {
            return "Неизвестный пользователь";
        }

        String lastName = user.getLastName();
        String firstName = user.getFirstName();
        String patronymic = user.getPatronymic();

        String result = lastName;
        if (firstName != null && !firstName.isEmpty()) {
            result += " " + firstName.charAt(0) + ".";
        }
        if (patronymic != null && !patronymic.isEmpty()) {
            result += patronymic.charAt(0) + ".";
        }

        return result;
    }

    /**
     * Получает студента по имени пользователя
     *
     * @param userName Логин студента
     * @return Найденный студент или null если не найден
     */
    public Student getStudentByUsername(String userName) {
        return studentRepository.findByUserName(userName).orElse(null);
    }

    /**
     * Получает преподавателя по имени пользователя
     *
     * @param userName Логин преподавателя
     * @return Найденный преподаватель или null если не найден
     */
    public Teacher getTeacherByUsername(String userName) {
        return teacherRepository.findByUserName(userName).orElse(null);
    }

    /**
     * Получает администратора по имени пользователя
     *
     * @param userName Логин администратора
     * @return Найденный администратор или null если не найден
     */
    public Admin getAdminByUsername(String userName) {
        return adminRepository.findByUserName(userName).orElse(null);
    }

    // ========== СОХРАНЕНИЕ ПОЛЬЗОВАТЕЛЯ В БАЗЕ ДАННЫХ ==========

    /**
     * Сохраняет пользователя в соответствующем репозитории
     *
     * @param user Пользователь для сохранения
     * @return Сохраненный пользователь
     */
    public User save(User user) {
        if (user instanceof Student) {
            return studentRepository.save((Student) user);
        } else if (user instanceof Teacher) {
            return teacherRepository.save((Teacher) user);
        } else if (user instanceof Admin) {
            return adminRepository.save((Admin) user);
        } else {
            return userRepository.save(user);
        }
    }

    /**
     * Получает список студентов по факультету
     *
     * @param faculty Название факультета
     * @return Список студентов указанного факультета
     */
    public List<Student> getStudentsByFaculty(String faculty) {
        return studentRepository.findByFaculty(faculty);
    }

    /**
     * Получает список студентов по факультету преподавателя
     *
     * @param teacherId Идентификатор преподавателя
     * @return Список студентов того же факультета, что и преподаватель
     */
    public List<Student> getStudentsByTeacherFaculty(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId).orElse(null);
        if (teacher == null) {
            return new ArrayList<>();
        }
        return studentRepository.findByFaculty(teacher.getFaculty());
    }
}