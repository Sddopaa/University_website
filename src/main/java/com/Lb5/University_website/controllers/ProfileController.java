package com.Lb5.University_website.controllers;

import com.Lb5.University_website.models.User;
import com.Lb5.University_website.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.*;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    private final String AVATAR_DIR = "src/main/resources/static/images/userAvatar/";
    private final String DEFAULT_EXTENSION = ".jpg"; // Все файлы будем сохранять как .jpg

    @PostMapping("/profile/upload-avatar")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile file,
                               HttpSession session,
                               RedirectAttributes redirect) {

        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) return "redirect:/login";

        try {
            User user = userService.getUserByUsername(sessionUser.getUserName());

            // Проверка 1: файл не пустой
            if (file.isEmpty()) {
                redirect.addFlashAttribute("error", "Файл не выбран");
                return "redirect:/profile";
            }

            // Проверка 2: файл является картинкой
            if (!file.getContentType().startsWith("image/")) {
                redirect.addFlashAttribute("error", "Только изображения");
                return "redirect:/profile";
            }

            // ВСЕГДА используем .jpg расширение, независимо от исходного файла
            String fileName = user.getUserName() + DEFAULT_EXTENSION; // всегда user123.jpg

            Path dir = Paths.get(AVATAR_DIR);

            // Создаем папку если не существует
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            // Полный путь к файлу
            Path filePath = dir.resolve(fileName);

            // Удаляем старый файл если существует
            Files.deleteIfExists(filePath);

            // Сохраняем файл ВСЕГДА с расширением .jpg
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Обновляем путь в базе данных (всегда будет .jpg)
            user.setAvatarPath("/images/userAvatar/" + fileName);
            userService.save(user);

            // Обновляем сессию
            session.setAttribute("user", user);

            redirect.addFlashAttribute("success", "Аватар обновлен");

        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Ошибка: " + e.getMessage());
        }

        return "redirect:/profile";
    }

    @PostMapping("/profile/remove-avatar")
    public String removeAvatar(HttpSession session, RedirectAttributes redirect) {

        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) return "redirect:/login";

        try {
            User user = userService.getUserByUsername(sessionUser.getUserName());

            // Устанавливаем дефолтную аватарку
            user.setDefaultAvatarPath();
            userService.save(user);

            // Обновляем сессию
            session.setAttribute("user", user);
            redirect.addFlashAttribute("success", "Аватар удален");

        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Ошибка удаления");
        }

        return "redirect:/profile";
    }
}

