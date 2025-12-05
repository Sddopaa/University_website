package com.Lb5.University_website.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Teacher extends User {

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "faculty", nullable = false)
    private String faculty;

    @Column(name = "work_experience", nullable = false)
    private Integer workExperience;

    public Teacher() {
        super();
    }

    public Teacher(String userName, String password,
                   String firstName, String lastName, String patronymic, int age,
                   String subject, String department, String faculty, int workExperience) {
        super(userName, password, "teacher", firstName, lastName, patronymic, age);

        setSubject(subject);
        setDepartment(department);
        setFaculty(faculty);
        setWorkExperience(workExperience);
    }

    public String getSubject() { return subject; }
    public String getDepartment() { return department; }
    public String getFaculty() { return faculty; }
    public Integer getWorkExperience() { return workExperience; }

    // Сеттеры
    public void setSubject(String subject) {
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("Предмет не может быть пустым");
        }
        this.subject = subject.trim();
    }

    public void setDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Кафедра не может быть пустой");
        }
        this.department = department.trim();
    }

    public void setFaculty(String faculty) {
        if (faculty == null || faculty.trim().isEmpty()) {
            throw new IllegalArgumentException("Факультет не может быть пустым");
        }
        this.faculty = faculty.trim();
    }

    public void setWorkExperience(Integer workExperience) {
        if (workExperience == null || workExperience < 0 || workExperience > 50) {
            throw new IllegalArgumentException("Стаж работы должен быть от 0 до 50 лет");
        }
        if (getAge() - workExperience < 16) {
            throw new IllegalArgumentException("Некорректный рабочий стаж для указанного возраста");
        }
        this.workExperience = workExperience;
    }
}