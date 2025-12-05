package com.Lb5.University_website.services;

import com.Lb5.University_website.models.Task;
import com.Lb5.University_website.models.User;
import com.Lb5.University_website.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task createTask(String title, String description, String subject,
                           Long teacherId, Long studentId) {

        // Простая проверка существования пользователей
        User teacher = userService.getUserById(teacherId);
        User student = userService.getUserById(studentId);
        if (student == null) {
            throw new RuntimeException("Студент не найден");
        }
        String teacherName = userService.getUserFullNameById(teacherId);
        String studentName = userService.getUserFullNameById(studentId);
        Task task = new Task(title, description, subject,
                teacherId, teacherName,
                studentId, studentName);
        return taskRepository.save(task);
    }


    public List<Task> getTeacherTasks(Long teacherId) {
        return taskRepository.findByTeacherId(teacherId);
    }

    public List<Task> getStudentTasks(Long studentId) {
        return taskRepository.findByStudentId(studentId);
    }


    public boolean deleteTask(Long taskId, Long teacherId) {
        Task task = taskRepository.findByIdAndTeacherId(taskId, teacherId);
        if (task != null) {
            taskRepository.delete(task);
            return true;
        }
        return false;
    }


    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Задание не найдено"));
    }

    // ============= ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ (опционально) =============

    /**
     * Проверить, может ли учитель удалить задание
     */
    public boolean canTeacherDeleteTask(Long taskId, Long teacherId) {
        return taskRepository.findByIdAndTeacherId(taskId, teacherId) != null;
    }

    /**
     * Получить количество заданий у учителя
     */
    public Long getTeacherTaskCount(Long teacherId) {
        return taskRepository.countByTeacherId(teacherId);
    }

    /**
     * Получить количество заданий у студента
     */
    public Long getStudentTaskCount(Long studentId) {
        return taskRepository.countByStudentId(studentId);
    }
}