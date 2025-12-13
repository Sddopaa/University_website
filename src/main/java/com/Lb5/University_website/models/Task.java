package com.Lb5.University_website.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Модель задания, выдаваемого преподавателем студенту
 */
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // Уникальный идентификатор задания

    @Column(name = "title", nullable = false, length = 50)
    private String title; // Заголовок задания (3-50 символов)

    @Column(name = "description", nullable = false, length = 200)
    private String description; // Описание задания (20-200 символов)

    @Column(name = "subject", nullable = false, length = 50)
    private String subject; // Учебный предмет

    @Column(name = "teacher_id", nullable = false)
    private Long teacherId; // ID преподавателя, создавшего задание

    @Column(name = "teacher_name", nullable = false)
    private String teacherName; // Имя преподавателя

    @Column(name = "student_id", nullable = false)
    private Long studentId; // ID студента, которому назначено задание

    @Column(name = "student_name", nullable = false)
    private String studentName; // Имя студента

    @ElementCollection
    @CollectionTable(
            name = "task_attachments",
            joinColumns = @JoinColumn(name = "task_id")
    )
    @Column(name = "file_path")
    private List<String> attachmentPaths = new ArrayList<>(); // Пути к прикрепленным файлам

    public Task() {}

    /**
     * Конструктор с параметрами для создания задания
     *
     * @param title         Заголовок задания
     * @param description   Описание задания
     * @param subject       Учебный предмет
     * @param teacherId     ID преподавателя
     * @param teacherName   Имя преподавателя
     * @param studentId     ID студента
     * @param studentName   Имя студента
     */
    public Task(String title, String description, String subject,
                Long teacherId, String teacherName,
                Long studentId, String studentName) {
        setTitle(title);
        setDescription(description);
        setSubject(subject);
        setTeacherId(teacherId);
        setTeacherName(teacherName);
        setStudentId(studentId);
        setStudentName(studentName);
        this.attachmentPaths = new ArrayList<>(); // Инициализируем пустой список
    }

    // ----------------- Геттеры и сеттеры -----------------

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

    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    // ----------------- Методы для работы с файлами -----------------

    public List<String> getAttachmentPaths() {
        return attachmentPaths;
    }

    public void setAttachmentPaths(List<String> attachmentPaths) {
        this.attachmentPaths = attachmentPaths != null ? attachmentPaths : new ArrayList<>();
    }

    public void addAttachmentPath(String filePath) {
        if (filePath != null && !filePath.trim().isEmpty()) {
            if (this.attachmentPaths == null) {
                this.attachmentPaths = new ArrayList<>();
            }
            this.attachmentPaths.add(filePath.trim());
        }
    }

    public boolean removeAttachmentPath(String filePath) {
        return this.attachmentPaths != null && filePath != null && this.attachmentPaths.remove(filePath);
    }

    public boolean hasAttachments() {
        return this.attachmentPaths != null && !this.attachmentPaths.isEmpty();
    }

    public int getAttachmentCount() {
        return this.attachmentPaths != null ? this.attachmentPaths.size() : 0;
    }
}
