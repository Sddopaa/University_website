package com.Lb5.University_website.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Модель преподавателя университета
 * Наследует общие атрибуты от базового класса User
 */
@Entity
@Table(name = "teachers")
@PrimaryKeyJoinColumn(name = "user_id") // Связь по первичному ключу с таблицей users
public class Teacher extends User {

    @Column(name = "subject", nullable = false)
    private String subject; // Преподаваемый предмет

    @Column(name = "department", nullable = false)
    private String department; // Кафедра

    @Column(name = "faculty", nullable = false)
    private String faculty; // Факультет

    @Column(name = "work_experience", nullable = false)
    private Integer workExperience; // Стаж работы в годах

    /**
     * Конструктор по умолчанию, необходимый для JPA
     */
    public Teacher() {
        super();
    }

    /**
     * Конструктор с параметрами для создания преподавателя
     *
     * @param userName          Логин преподавателя
     * @param password          Пароль преподавателя
     * @param firstName         Имя преподавателя
     * @param lastName          Фамилия преподавателя
     * @param patronymic        Отчество преподавателя
     * @param age               Возраст преподавателя
     * @param subject           Преподаваемый предмет
     * @param department        Кафедра
     * @param faculty           Факультет
     * @param workExperience    Стаж работы (в годах)
     */
    public Teacher(String userName, String password,
                   String firstName, String lastName, String patronymic, int age,
                   String subject, String department, String faculty, int workExperience) {
        super(userName, password, "teacher", firstName, lastName, patronymic, age);

        // Установка атрибутов через сеттеры для валидации
        setSubject(subject);
        setDepartment(department);
        setFaculty(faculty);
        setWorkExperience(workExperience);
    }

    // Геттеры
    public String getSubject() { return subject; }
    public String getDepartment() { return department; }
    public String getFaculty() { return faculty; }
    public Integer getWorkExperience() { return workExperience; }

    /**
     * Устанавливает преподаваемый предмет
     *
     * @param subject Название предмета (не может быть null или пустым)
     * @throws IllegalArgumentException Если предмет null или пустой
     */
    public void setSubject(String subject) {
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("Предмет не может быть пустым");
        }
        this.subject = subject.trim();
    }

    /**
     * Устанавливает кафедру преподавателя
     *
     * @param department Название кафедры (не может быть null или пустым)
     * @throws IllegalArgumentException Если кафедра null или пустая
     */
    public void setDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Кафедра не может быть пустой");
        }
        this.department = department.trim();
    }

    /**
     * Устанавливает факультет преподавателя
     *
     * @param faculty Название факультета (не может быть null или пустым)
     * @throws IllegalArgumentException Если факультет null или пустой
     */
    public void setFaculty(String faculty) {
        if (faculty == null || faculty.trim().isEmpty()) {
            throw new IllegalArgumentException("Факультет не может быть пустым");
        }
        this.faculty = faculty.trim();
    }

    /**
     * Устанавливает стаж работы преподавателя
     *
     * @param workExperience Стаж работы в годах (0-50 лет)
     * @throws IllegalArgumentException Если стаж вне допустимого диапазона или
     *         не соответствует возрасту преподавателя
     */
    public void setWorkExperience(Integer workExperience) {
        // Проверка диапазона стажа
        if (workExperience == null || workExperience < 0 || workExperience > 50) {
            throw new IllegalArgumentException("Стаж работы должен быть от 0 до 50 лет");
        }

        // Проверка соответствия стажа возрасту (нельзя начать работать раньше 14 лет)
        if (getAge() - workExperience < 14) {
            throw new IllegalArgumentException("Некорректный рабочий стаж для указанного возраста");
        }

        this.workExperience = workExperience;
    }
}