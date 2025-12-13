package com.Lb5.University_website.services;

import com.Lb5.University_website.models.Task;
import com.Lb5.University_website.models.User;
import com.Lb5.University_website.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Transactional
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    private final Path rootDir = Paths.get("tasksFiles");

    /**
     * Создает новое задание
     *
     * @param title Название задания
     * @param description Описание задания
     * @param subject Предмет
     * @param teacherId Идентификатор преподавателя
     * @param studentId Идентификатор студента
     * @return Созданное задание
     * @throws RuntimeException если студент не найден
     */
    public Task createTask(String title, String description, String subject, Long teacherId, Long studentId) {
        User teacher = userService.getUserById(teacherId);
        User student = userService.getUserById(studentId);
        if (student == null) throw new RuntimeException("Студент не найден");

        String teacherName = userService.getUserFullNameById(teacherId);
        String studentName = userService.getUserFullNameById(studentId);

        Task task = new Task(title, description, subject,
                teacherId, teacherName,
                studentId, studentName);

        return taskRepository.save(task);
    }

    /**
     * Получает список заданий преподавателя
     *
     * @param teacherId Идентификатор преподавателя
     * @return Список заданий
     */
    public List<Task> getTeacherTasks(Long teacherId) {
        return taskRepository.findByTeacherId(teacherId);
    }

    /**
     * Получает список заданий студента
     *
     * @param studentId Идентификатор студента
     * @return Список заданий
     */
    public List<Task> getStudentTasks(Long studentId) {
        return taskRepository.findByStudentId(studentId);
    }

    /**
     * Удаляет задание, если оно принадлежит указанному преподавателю
     *
     * @param taskId Идентификатор задания
     * @param teacherId Идентификатор преподавателя
     * @return true если задание удалено, false если не найдено или нет прав
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
     * Получает задание по идентификатору
     *
     * @param taskId Идентификатор задания
     * @return Найденное задание
     * @throws RuntimeException если задание не найдено
     */
    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Задание не найдено"));
    }

    /**
     * Сохраняет задание
     *
     * @param task Задание для сохранения
     * @return Сохраненное задание
     */
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    /**
     * Сохраняет файл, связанный с заданием
     *
     * @param file Файл для сохранения
     * @param taskId Идентификатор задания
     * @return Безопасное имя сохраненного файла
     * @throws IOException если произошла ошибка ввода-вывода
     * @throws IllegalArgumentException если файл пустой
     */
    public String saveFile(MultipartFile file, Long taskId) throws IOException {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("Файл не может быть пустым");

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) originalFilename = "unknown";

        // Очистка имени файла от небезопасных символов и добавление префикса taskId
        String safeFilename = taskId + "_" + originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");

        if (!Files.exists(rootDir)) {
            Files.createDirectories(rootDir);
        }

        Path destination = rootDir.resolve(safeFilename);
        file.transferTo(destination);

        return safeFilename;
    }

    /**
     * Получает путь к файлу по его имени
     *
     * @param fileName Имя файла
     * @return Полный путь к файлу
     */
    public Path getFilePath(String fileName) {
        return rootDir.resolve(fileName);
    }

    /**
     * Получает ресурс файла по его имени
     *
     * @param fileName Имя файла
     * @return Ресурс файла
     */
    public Resource getResource(String fileName) {
        Path path = getFilePath(fileName);
        return new FileSystemResource(path.toFile());
    }
}