package com.Lb5.University_website.repository;

import com.Lb5.University_website.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // 1. Все задания учителя
    List<Task> findByTeacherId(Long teacherId);

    // 2. Все задания студента
    List<Task> findByStudentId(Long studentId);

    // 3. Поиск по ID задания с проверкой учителя
    Task findByIdAndTeacherId(Long id, Long teacherId);
}