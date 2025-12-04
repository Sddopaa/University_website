package com.Lb5.University_website.repository;

import com.Lb5.University_website.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // 1. Все задания учителя (для страницы "Мои задания" учителя)
    List<Task> findByTeacherId(Long teacherId);

    // 2. Все задания студента (для страницы "Мои задания" студента)
    List<Task> findByStudentId(Long studentId);

    // 4. Поиск по ID задания с проверкой учителя (для безопасного удаления)
    // Используется для: "Удалить задание, но только если оно мое"
    Task findByIdAndTeacherId(Long id, Long teacherId);

    // 5. Количество заданий у учителя (может пригодиться для статистики)
    Long countByTeacherId(Long teacherId);

    // 6. Количество заданий у студента
    Long countByStudentId(Long studentId);
}