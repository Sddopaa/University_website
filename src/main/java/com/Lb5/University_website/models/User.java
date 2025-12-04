package com.Lb5.University_website.models;

import jakarta.persistence.*;


@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String userName;

    @Column(nullable = false, length = 50)
    private String password;

    @Column(nullable = false, length = 20)
    private String role;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "patronymic", length = 50)
    private String patronymic;

    @Column(nullable = false)
    private Integer age;

    @Column(name = "avatar_path")
    private String avatarPath = "/images/defaultAvatar.png";

    public User() {};

    public User(String userName, String password, String role,
                String firstName, String lastName, String patronymic, int age) {
        setUserName(userName);
        setPassword(password);
        setRole(role);
        setFIO(lastName, firstName, patronymic);
        setAge(age);
    }

    // Геттеры остаются без изменений
    public Long getId() { return id; }
    public String getUserName() { return userName; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPatronymic() { return patronymic; }
    public Integer getAge() { return age; }
    public String getAvatarPath() { return avatarPath; }

    // Сеттеры
    public void setFIO(String lastName, String firstName, String patronymic) {
        setLastName(lastName);
        setFirstName(firstName);
        setPatronymic(patronymic);
    }

    public void setUserName(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Логин не может быть пустым");
        }
        if (username.length() < 3 || username.length() > 50) {
            throw new IllegalArgumentException("Логин должен быть от 3 до 50 символов");
        }
        this.userName = username.trim();
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 3) {
            throw new IllegalArgumentException("Пароль должен быть не менее 3 символов");
        }
        this.password = password;
    }

    public void setRole(String role) {
        if (role == null || (!role.equals("student") && !role.equals("teacher") && !role.equals("admin"))) {
            throw new IllegalArgumentException("Недопустимая роль пользователя");
        }
        this.role = role;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        this.firstName = firstName.trim();
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Фамилия не может быть пустой");
        }
        this.lastName = lastName.trim();
    }

    public void setPatronymic(String patronymic) {
        if (patronymic == null || patronymic.trim().isEmpty()) {
            this.patronymic = null;
        } else {
            this.patronymic = patronymic.trim();
        }
    }

    public void setAge(Integer age) {
        if (age == null || age < 17 || age > 70) {
            throw new IllegalArgumentException("Возраст должен быть от 17 до 70 лет");
        }
        this.age = age;
    }

    public void setDefaultAvatarPath() {
        this.avatarPath = "images/defaultAvatar.png";
    }

    public void setAvatarPath(String path) {
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("Некорректный путь к аватарке");
        }
        this.avatarPath = path;
    }
}