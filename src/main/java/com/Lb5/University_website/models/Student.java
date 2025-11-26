package com.Lb5.University_website.models;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "user_id")
public class Student extends User {

    @Column(name = "course")
    private Byte course;

    @Column(name = "is_tuition_free")
    private Boolean isTuitionFree;

    @Column(name = "faculty")
    private String faculty;

    @Column(name = "Specialization")
    private String specialization;

    public Student(String userName, String password,
                   String firstName, String lastName, String patronymic, int age,
                   byte course, boolean isTuitionFree, String faculty, String specialization) {
        super(userName, password, "student", firstName, lastName, patronymic, age);
        setCourse(course);
        setFaculty(faculty);
        setSpecialization(specialization);
        setTuitionFree(isTuitionFree);
    }

    public Byte getCourse() { return course; }
    public Boolean getTuitionFree() { return isTuitionFree; }
    public String getFaculty() { return faculty; }
    public String getSpecialization() { return specialization; }

    public void setSpecialization(String specialization) {
        if (specialization == null || specialization.trim().isEmpty()) {
            throw new IllegalArgumentException("Факультет не может быть пустым");
        }
        this.specialization = specialization;
    }
    public void setTuitionFree(Boolean isTuitionFree) {
        this.isTuitionFree = isTuitionFree;
    }

    public void setFaculty(String faculty) {
        if (faculty == null || faculty.trim().isEmpty()) {
            throw new IllegalArgumentException("Факультет не может быть пустым");
        }
        this.faculty = faculty.trim();
    }
    public void setCourse(Byte course) {
        if (course < 1 || course > 6) {
            throw new IllegalArgumentException("Неверный курс. Должен быть от 1 до 6.");
        }
        this.course = course;
    }
}