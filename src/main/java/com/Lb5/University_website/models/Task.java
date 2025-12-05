package com.Lb5.University_website.models;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "description", nullable = false, length = 200)
    private String description;

    @Column(name = "subject", nullable = false, length = 50)
    private String subject;

    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;

    @Column(name = "teacher_name", nullable = false)
    private String teacherName;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    public Task() {}

    public Task(String title, String description, String subject, Long teacherId, String teacherName, Long studentId, String studentName) {
        setTitle(title);
        setDescription(description);
        setSubject(subject);
        setTeacherId(teacherId);
        setTeacherName(teacherName);
        setStudentId(studentId);
        setStudentName(studentName);
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) {
        if (title == null || title.length() < 3 || title.length() > 50) {
            throw new IllegalArgumentException("Наименование задания должно быть от 3 до 50 символов");
        }
        this.title = title;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        if (description == null || description.length() < 20 || description.length() > 200) {
            throw new IllegalArgumentException("Текст задания должен быть от 20 до 200 символов");
        }
        this.description = description;
    }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
}