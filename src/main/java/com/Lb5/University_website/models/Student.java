package com.Lb5.University_website.models;
import jakarta.persistence.*;

/**
 * Модель студента университета
 */
@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "user_id") // Связь по первичному ключу с таблицей users
public class Student extends User {

    @Column(name = "course")
    private Byte course; // Курс обучения (1-6)

    @Column(name = "is_tuition_free")
    private Boolean isTuitionFree; // Признак бесплатного обучения

    @Column(name = "faculty")
    private String faculty; // Название факультета

    @Column(name = "Specialization")
    private String specialization; // Специализация/направление

    public Student() { super(); }

    /**
     * Конструктор с параметрами для создания студента
     *
     * @param userName          Логин студента
     * @param password          Пароль студента
     * @param firstName         Имя студента
     * @param lastName          Фамилия студента
     * @param patronymic        Отчество студента
     * @param age               Возраст студента
     * @param course            Курс обучения (1-6)
     * @param isTuitionFree     Признак бесплатного обучения
     * @param faculty           Факультет
     * @param specialization    Специализация
     */
    public Student(String userName, String password,
                   String firstName, String lastName, String patronymic, int age,
                   byte course, boolean isTuitionFree, String faculty, String specialization) {
        super(userName, password, "student", firstName, lastName, patronymic, age);

        // Установка атрибутов через сеттеры для валидации
        setCourse(course);
        setFaculty(faculty);
        setSpecialization(specialization);
        setTuitionFree(isTuitionFree);
    }

    // Геттеры
    public Byte getCourse() { return course; }
    public Boolean getTuitionFree() { return isTuitionFree; }
    public String getFaculty() { return faculty; }
    public String getSpecialization() { return specialization; }

    /**
     * Устанавливает специализацию студента
     *
     * @param specialization Специализация (не может быть null или пустой)
     * @throws IllegalArgumentException Если специализация null или пустая
     */
    public void setSpecialization(String specialization) {
        if (specialization == null || specialization.trim().isEmpty()) {
            throw new IllegalArgumentException("Факультет не может быть пустым");
        }
        this.specialization = specialization;
    }

    /**
     * Устанавливает признак бесплатного обучения
     *
     * @param isTuitionFree true - бюджетная форма, false - платная форма
     */
    public void setTuitionFree(Boolean isTuitionFree) {
        this.isTuitionFree = isTuitionFree;
    }

    /**
     * Устанавливает факультет студента
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
     * Устанавливает курс обучения
     *
     * @param course Курс обучения (должен быть в диапазоне 1-6)
     * @throws IllegalArgumentException Если курс вне допустимого диапазона
     */
    public void setCourse(Byte course) {
        if (course < 1 || course > 6) {
            throw new IllegalArgumentException("Неверный курс. Должен быть от 1 до 6.");
        }
        this.course = course;
    }
}