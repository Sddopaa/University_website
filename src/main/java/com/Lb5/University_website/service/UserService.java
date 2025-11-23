package com.Lb5.University_website.service;

import com.Lb5.University_website.models.User;
import com.Lb5.University_website.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String userName, String password, String role,
                             String firstName, String lastName, String patronymic, int age) {
        if (userRepository.existsByUserName(userName)) {
            throw new IllegalArgumentException("Логин '" + userName + "' уже занят");
        }
        User user = new User(userName, password, role, firstName, lastName, patronymic, age);
        return userRepository.save(user);
    }

    public User loginUser(String userName, String password) {
        return userRepository.findByUserName(userName)
                .filter(user -> user.getPassword().equals(password)) // Проверяем пароль
                .orElse(null); // Если не нашли или пароль неверный - возвращаем null
    }
}