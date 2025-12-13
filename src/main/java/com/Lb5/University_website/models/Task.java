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

    @ElementCollection
    @CollectionTable(
            name = "task_attachments",
            joinColumns = @JoinColumn(name = "task_id")
    )
    @Column(name = "file_path")
    private List<String> attachmentPaths = new ArrayList<>();

    public Task() {}

    /**
     * Создает новое задание с указанными параметрами
     *
     * @param title Заголовок задания (3-50 символов)
     * @param description Описание задания (20-200 символов)
     * @param subject Учебный предмет
     * @param teacherId Идентификатор преподавателя
     * @param teacherName Имя преподавателя
     * @param studentId Идентификатор студента
     * @param studentName Имя студента
     * @throws IllegalArgumentException если параметры не соответствуют ограничениям
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
        this.attachmentPaths = new ArrayList<>();
    }

    // --- Стандартные геттеры и сеттеры ---
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

    // --- Методы для работы с файлами ---

    public List<String> getAttachmentPaths() {
        return attachmentPaths;
    }

    public void setAttachmentPaths(List<String> attachmentPaths) {
        this.attachmentPaths = attachmentPaths != null ? attachmentPaths : new ArrayList<>();
    }

    /**
     * Добавляет путь к прикрепленному файлу
     *
     * @param filePath Путь к файлу
     */
    public void addAttachmentPath(String filePath) {
        if (filePath != null && !filePath.trim().isEmpty()) {
            if (this.attachmentPaths == null) {
                this.attachmentPaths = new ArrayList<>();
            }
            this.attachmentPaths.add(filePath.trim());
        }
    }

    /**
     * Удаляет путь к прикрепленному файлу
     *
     * @param filePath Путь к файлу для удаления
     * @return true если файл был удален, false если не найден
     */
    public boolean removeAttachmentPath(String filePath) {
        return this.attachmentPaths != null && filePath != null && this.attachmentPaths.remove(filePath);
    }

    /**
     * Проверяет наличие прикрепленных файлов
     *
     * @return true если есть прикрепленные файлы
     */
    public boolean hasAttachments() {
        return this.attachmentPaths != null && !this.attachmentPaths.isEmpty();
    }

    /**
     * Получает количество прикрепленных файлов
     *
     * @return Количество файлов
     */
    public int getAttachmentCount() {
        return this.attachmentPaths != null ? this.attachmentPaths.size() : 0;
    }
}