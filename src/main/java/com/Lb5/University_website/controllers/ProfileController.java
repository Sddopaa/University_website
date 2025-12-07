package com.Lb5.University_website.controllers;

import com.Lb5.University_website.models.User;
import com.Lb5.University_website.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

import java.nio.file.*;

/**
 * Контроллер для обработки операций с профилем пользователя
 */
@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    // Директория для сохранения аватаров пользователей
    private final String AVATAR_DIR = "src/main/resources/static/images/userAvatar/";

    // Расширение по умолчанию для всех загружаемых изображений
    private final String DEFAULT_EXTENSION = ".jpg";

    /**
     * Обрабатывает загрузку аватара пользователя
     *
     * @param file          Загружаемый файл изображения
     * @param session       Сессия HTTP для проверки авторизации
     * @param redirect      Атрибуты для передачи сообщений при редиректе
     * @return Редирект на страницу профиля с сообщением о результате
     */
    @PostMapping("/profile/upload-avatar")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile file,
                               HttpSession session,
                               RedirectAttributes redirect) {

        // Проверка авторизации пользователя
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) return "redirect:/login";

        try {
            // Получение актуальных данных пользователя из базы
            User user = userService.getUserByUsername(sessionUser.getUserName());

            // Проверка 1: файл не пустой
            if (file.isEmpty()) {
                redirect.addFlashAttribute("error", "Файл не выбран");
                return "redirect:/profile";
            }

            // Проверка 2: файл является изображением
            if (!file.getContentType().startsWith("image/")) {
                redirect.addFlashAttribute("error", "Только изображения");
                return "redirect:/profile";
            }

            // Формирование имени файла: username + .jpg
            String fileName = user.getUserName() + DEFAULT_EXTENSION;
            Path dir = Paths.get(AVATAR_DIR);

            // Создание директории если она не существует
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            // Формирование полного пути к файлу
            Path filePath = dir.resolve(fileName);

            // Удаление старого файла если он существует
            Files.deleteIfExists(filePath);

            // Сохранение нового файла
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Обновление пути к аватару в базе данных
            user.setAvatarPath("/images/userAvatar/" + fileName);
            userService.save(user);

            // Обновление данных пользователя в сессии
            session.setAttribute("user", user);
            redirect.addFlashAttribute("success", "Аватар обновлен");

        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Ошибка: " + e.getMessage());
        }

        return "redirect:/profile";
    }

    /**
     * Обрабатывает удаление аватара пользователя
     *
     * @param session  Сессия HTTP для проверки авторизации
     * @param redirect Атрибуты для передачи сообщений при редиректе
     * @return Редирект на страницу профиля с сообщением о результате
     */
    @PostMapping("/profile/remove-avatar")
    public String removeAvatar(HttpSession session, RedirectAttributes redirect) {

        // Проверка авторизации пользователя
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) return "redirect:/login";

        try {
            // Получение актуальных данных пользователя из базы
            User user = userService.getUserByUsername(sessionUser.getUserName());

            // Установка аватара по умолчанию
            user.setDefaultAvatarPath();
            userService.save(user);

            // Обновление данных пользователя в сессии
            session.setAttribute("user", user);
            redirect.addFlashAttribute("success", "Аватар удален");

        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Ошибка удаления");
        }

        return "redirect:/profile";
    }
}