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

@Service
public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final AdminRepository adminRepository;

    public UserService(UserRepository userRepository,
                       StudentRepository studentRepository,
                       TeacherRepository teacherRepository,
                       AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.adminRepository = adminRepository;
    }

    public User registerUser(String userName, String password, String role,
                             String firstName, String lastName, String patronymic, int age,
                             Byte course, Boolean isTuitionFree, String faculty, String specialization,
                             String subject, String department, Integer workExperience) {

        if (userRepository.existsByUserName(userName)) {
            throw new IllegalArgumentException("Пользователь с именем '" + userName + "' уже занят");
        }

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

    public User loginUser(String userName, String password) {
        return userRepository.findByUserName(userName)
                .filter(user -> user.getPassword().equals(password))
                .orElse(null);
    }

    // ПРОСТОЙ МЕТОД - возвращаем всегда User из userRepository
    public User getUserByUsername(String userName) {
        return userRepository.findByUserName(userName).orElse(null);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ ДЛЯ КОНКРЕТНЫХ ТИПОВ
    public Student getStudentByUsername(String userName) {
        return studentRepository.findByUserName(userName).orElse(null);
    }

    public Teacher getTeacherByUsername(String userName) {
        return teacherRepository.findByUserName(userName).orElse(null);
    }

    public Admin getAdminByUsername(String userName) {
        return adminRepository.findByUserName(userName).orElse(null);
    }

    // ========== СОХРАНЕНИЕ ПОЛЬЗОВАТЕЛЯ В БАЗЕ ДАННЫХ ==========
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
}