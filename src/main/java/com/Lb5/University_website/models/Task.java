package com.Lb5.University_website.models;

import jakarta.persistence.*;

@Entity
@Table(name = "student_tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;

    @Column(name = "task_text", nullable = false, columnDefinition = "TEXT")
    private String taskText;

    public Task(Long studentId, Long teacherId, String taskText) {
        setStudentId(studentId);
        setTeacherId(teacherId);
        setTaskText(taskText);
    }

    public Long getId() { return id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }

    public String getTaskText() { return taskText; }
    public void setTaskText(String taskText) { this.taskText = taskText; }

}