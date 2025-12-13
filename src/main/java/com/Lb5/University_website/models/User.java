package com.Lb5.University_website.models;

import jakarta.persistence.*;

/**
 * Базовый класс пользователя системы.
 * Использует стратегию наследования JOINED для хранения общих данных в этой таблице
 * и специфичных данных в дочерних таблицах (students, teachers, admins)
 */
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // Уникальный идентификатор пользователя

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String userName; // Логин пользователя (уникальный)

    @Column(nullable = false, length = 50)
    private String password; // Пароль пользователя

    @Column(nullable = false, length = 20)
    private String role; // Роль пользователя: student, teacher, admin

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName; // Имя пользователя

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName; // Фамилия пользователя

    @Column(name = "patronymic", length = 50)
    private String patronymic; // Отчество пользователя (опционально)

    @Column(nullable = false)
    private Integer age; // Возраст пользователя

    @Column(name = "avatar_path")
    private String avatarPath = "/avatars/defaultAvatar.png";

    /**
     * Конструктор по умолчанию, необходимый для JPA
     */
    public User() {};

    /**
     * Основной конструктор для создания пользователя
     *
     * @param userName    Логин пользователя
     * @param password    Пароль пользователя
     * @param role        Роль пользователя (student/teacher/admin)
     * @param firstName   Имя пользователя
     * @param lastName    Фамилия пользователя
     * @param patronymic  Отчество пользователя
     * @param age         Возраст пользователя
     */
    public User(String userName, String password, String role,
                String firstName, String lastName, String patronymic, int age) {
        setUserName(userName);
        setPassword(password);
        setRole(role);
        setFIO(lastName, firstName, patronymic);
        setAge(age);
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

    /**
     * Устанавливает ФИО пользователя за один вызов
     *
     * @param lastName    Фамилия пользователя
     * @param firstName   Имя пользователя
     * @param patronymic  Отчество пользователя
     */
    public void setFIO(String lastName, String firstName, String patronymic) {
        setLastName(lastName);
        setFirstName(firstName);
        setPatronymic(patronymic);
    }

    /**
     * Устанавливает логин пользователя с валидацией
     *
     * @param username Логин пользователя (3-50 символов, не может быть пустым)
     * @throws IllegalArgumentException Если логин не соответствует требованиям
     */
    public void setUserName(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Логин не может быть пустым");
        }
        if (username.length() < 3 || username.length() > 50) {
            throw new IllegalArgumentException("Логин должен быть от 3 до 50 символов");
        }
        this.userName = username.trim();
    }

    /**
     * Устанавливает пароль пользователя с валидацией
     *
     * @param password Пароль пользователя (минимум 3 символа)
     * @throws IllegalArgumentException Если пароль не соответствует требованиям
     */
    public void setPassword(String password) {
        if (password == null || password.length() < 3) {
            throw new IllegalArgumentException("Пароль должен быть не менее 3 символов");
        }
        this.password = password;
    }

    /**
     * Устанавливает роль пользователя с валидацией
     *
     * @param role Роль пользователя (допустимые значения: student, teacher, admin)
     * @throws IllegalArgumentException Если роль не соответствует допустимым значениям
     */
    public void setRole(String role) {
        if (role == null || (!role.equals("student") && !role.equals("teacher") && !role.equals("admin"))) {
            throw new IllegalArgumentException("Недопустимая роль пользователя");
        }
        this.role = role;
    }

    /**
     * Устанавливает имя пользователя с валидацией
     *
     * @param firstName Имя пользователя (не может быть пустым)
     * @throws IllegalArgumentException Если имя null или пустое
     */
    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        this.firstName = firstName.trim();
    }

    /**
     * Устанавливает фамилию пользователя с валидацией
     *
     * @param lastName Фамилия пользователя (не может быть пустой)
     * @throws IllegalArgumentException Если фамилия null или пустая
     */
    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Фамилия не может быть пустой");
        }
        this.lastName = lastName.trim();
    }

    /**
     * Устанавливает отчество пользователя
     *
     * @param patronymic Отчество пользователя (может быть null или пустым)
     */
    public void setPatronymic(String patronymic) {
        if (patronymic == null || patronymic.trim().isEmpty()) {
            this.patronymic = null;
        } else {
            this.patronymic = patronymic.trim();
        }
    }

    /**
     * Устанавливает возраст пользователя с валидацией
     *
     * @param age Возраст пользователя (должен быть в диапазоне 17-70 лет)
     * @throws IllegalArgumentException Если возраст вне допустимого диапазона
     */
    public void setAge(Integer age) {
        if (age == null || age < 17 || age > 70) {
            throw new IllegalArgumentException("Возраст должен быть от 17 до 70 лет");
        }
        this.age = age;
    }

    /**
     * Устанавливает путь к аватару по умолчанию
     */
    public void setDefaultAvatarPath() {
        this.avatarPath = "images/defaultAvatar.png";
    }

    /**
     * Устанавливает путь к аватару пользователя
     *
     * @param path Путь к файлу аватара
     * @throws IllegalArgumentException Если путь null или пустой
     */
    public void setAvatarPath(String path) {
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("Некорректный путь к аватарке");
        }
        this.avatarPath = path;
    }
}