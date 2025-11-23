package com.Lb5.University_website.models;


import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(name = "first_name", nullable = false)
    private String firstName;


    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(nullable = false)
    private Integer age;

    @Column(name = "avatar_path")
    private String avatarPath = "images/defaultAvatar.png";

    // Конструкторы
    public User() {}

    public User(String userName, String password, String role,
                String firstName, String lastName, String patronymic, int age) {
        setUserName(userName);
        setPassword(password);
        setRole(role);
        setDefaultAvatarPath();
        setFIO(lastName, firstName, patronymic);
        setAge(age);
        this.avatarPath = "images/defaultAvatar.png";
    }

    // Геттеры
    public Long getId() { return id; }
    public String getUserName() { return userName; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPatronymic() { return patronymic; }
    public Integer getAge() { return age; }
    public String getAvatarPath() { return avatarPath; }
    //Сеттеры
    public void setUserName(String userName) {
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("Логин пользователя не может быть пустым");
        }
        this.userName = sanitizeInput(userName.trim());
    }

    public void setFIO(String lastName, String firstName, String patronymic) {
        setLastName(lastName);
        setFirstName(firstName);
        setPatronymic(patronymic);
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым");
        }
        this.firstName = sanitizeInput(firstName.trim());
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Фамилия пользователя не может быть пустым");
        }
        this.lastName = sanitizeInput(lastName.trim());
    }

    public void setPatronymic(String patronymic) {
        if (patronymic == null || patronymic.trim().isEmpty()) {
            this.patronymic = "";
        }
        this.patronymic = sanitizeInput(patronymic.trim());
    }

    public void setPassword(String password) {
        if (password == null || (password.length() < 3 || password.length() >= 20)) {
            throw new IllegalArgumentException("Пароль должен быть от 3 до 20 символов");
        }
        this.password = sanitizeInput(password.trim());
    }

    public void setRole(String role) {
        if (role == null || (!role.equals("admin") && !role.equals("teacher") && !role.equals("student"))) {
            throw new IllegalArgumentException("Недопустимая роль пользователя");
        }
        this.role = role;
    }

    public void setDefaultAvatarPath() {
        this.avatarPath = "images/defaultAvatar.png";
    }

    public void setAvatarPath(String path) {
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("Путь к аватарке не может быть пустым");
        }
        this.avatarPath = path;
    }

    public void setAge(int age) {
        if (age < 17 || age > 70) {
            throw new IllegalArgumentException("Возраст должен быть от 17 до 70 лет");
        }
        this.age = age;
    }

    //Метод для проверки инъекция
    private String sanitizeInput(String input) {
        if (input == null) return null;
        return input
                .replace("'", "''")
                .replace(";", "")
                .replace("--", "")
                .replace("/*", "")
                .replace("*/", "");
    }
}