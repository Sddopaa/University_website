package com.Lb5.University_website.services;

import com.Lb5.University_website.models.Task;
import com.Lb5.University_website.models.User;
import com.Lb5.University_website.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис для работы с заданиями.
 * Обеспечивает бизнес-логику создания, получения и удаления заданий
 */
@Service
@Transactional
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    /**
     * Создает новое задание для студента
     *
     * @param title       Заголовок задания
     * @param description Описание задания
     * @param subject     Учебный предмет
     * @param teacherId   ID преподавателя, создающего задание
     * @param studentId   ID студента, которому назначено задание
     * @return Созданное задание
     * @throws RuntimeException Если студент не найден
     */
    public Task createTask(String title, String description, String subject,
                           Long teacherId, Long studentId) {

        // Проверка существования пользователей
        User teacher = userService.getUserById(teacherId);
        User student = userService.getUserById(studentId);
        if (student == null) {
            throw new RuntimeException("Студент не найден");
        }

        // Получение полных имен преподавателя и студента
        String teacherName = userService.getUserFullNameById(teacherId);
        String studentName = userService.getUserFullNameById(studentId);

        // Создание и сохранение задания
        Task task = new Task(title, description, subject,
                teacherId, teacherName,
                studentId, studentName);
        return taskRepository.save(task);
    }

    /**
     * Получает список всех заданий, созданных преподавателем
     *
     * @param teacherId ID преподавателя
     * @return Список заданий преподавателя
     */
    public List<Task> getTeacherTasks(Long teacherId) {
        return taskRepository.findByTeacherId(teacherId);
    }

    /**
     * Получает список всех заданий, назначенных студенту
     *
     * @param studentId ID студента
     * @return Список заданий студента
     */
    public List<Task> getStudentTasks(Long studentId) {
        return taskRepository.findByStudentId(studentId);
    }

    /**
     * Удаляет задание с проверкой прав преподавателя
     *
     * @param taskId    ID задания для удаления
     * @param teacherId ID преподавателя, пытающегося удалить задание
     * @return true если задание удалено, false если задание не найдено или нет прав
     */
    public boolean deleteTask(Long taskId, Long teacherId) {
        Task task = taskRepository.findByIdAndTeacherId(taskId, teacherId);
        if (task != null) {
            taskRepository.delete(task);
            return true;
        }
        return false;
    }

    /**
     * Получает задание по его ID
     *
     * @param taskId ID задания
     * @return Найденное задание
     * @throws RuntimeException Если задание не найдено
     */
    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Задание не найдено"));
    }

    /**
     * Проверяет, может ли преподаватель удалить задание
     *
     * @param taskId    ID задания
     * @param teacherId ID преподавателя
     * @return true если преподаватель создал это задание, иначе false
     */
    public boolean canTeacherDeleteTask(Long taskId, Long teacherId) {
        return taskRepository.findByIdAndTeacherId(taskId, teacherId) != null;
    }
}