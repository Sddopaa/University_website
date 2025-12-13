package com.Lb5.University_website.controllers;

import com.Lb5.University_website.models.User;
import com.Lb5.University_website.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    private static final String AVATAR_DIR = "/app/userAvatar/";
    private static final String AVATAR_URL_PREFIX = "/avatars/";
    private static final String DEFAULT_AVATAR = "/avatars/defaultAvatar.png";

    /**
     * Загружает новый аватар пользователя
     *
     * @param file Загружаемый файл изображения
     * @param session HTTP-сессия
     * @param redirect Атрибуты для редиректа
     * @return Перенаправление на страницу профиля
     */
    @PostMapping("/profile/upload-avatar")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile file,
                               HttpSession session,
                               RedirectAttributes redirect) {

        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }

        try {
            User user = userService.getUserByUsername(sessionUser.getUserName());

            if (file == null || file.isEmpty()) {
                redirect.addFlashAttribute("error", "Файл не выбран");
                return "redirect:/profile";
            }

            if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
                redirect.addFlashAttribute("error", "Можно загружать только изображения");
                return "redirect:/profile";
            }

            // Определение расширения файла из оригинального имени
            String originalName = file.getOriginalFilename();
            String extension = ".jpg";
            if (originalName != null && originalName.contains(".")) {
                extension = originalName.substring(originalName.lastIndexOf("."));
            }

            String fileName = "user_" + user.getId() + extension;
            Path dirPath = Paths.get(AVATAR_DIR);

            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            // Удаление старого аватара (если это не дефолтный)
            if (user.getAvatarPath() != null && !user.getAvatarPath().equals(DEFAULT_AVATAR)) {
                String oldFileName = user.getAvatarPath()
                        .replace(AVATAR_URL_PREFIX, "")
                        .split("\\?")[0];
                Path oldFilePath = dirPath.resolve(oldFileName);
                Files.deleteIfExists(oldFilePath);
            }

            // Сохранение нового файла
            Path filePath = dirPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Добавление timestamp для обхода кэширования браузера
            String avatarUrl = AVATAR_URL_PREFIX + fileName + "?v=" + System.currentTimeMillis();
            user.setAvatarPath(avatarUrl);

            userService.save(user);
            session.setAttribute("user", user);

            redirect.addFlashAttribute("success", "Аватар успешно обновлён");

        } catch (Exception e) {
            e.printStackTrace();
            redirect.addFlashAttribute("error", "Ошибка загрузки аватара");
        }

        return "redirect:/profile";
    }

    /**
     * Удаляет аватар пользователя, устанавливая аватар по умолчанию
     *
     * @param session HTTP-сессия
     * @param redirect Атрибуты для редиректа
     * @return Перенаправление на страницу профиля
     */
    @PostMapping("/profile/remove-avatar")
    public String removeAvatar(HttpSession session, RedirectAttributes redirect) {

        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }

        try {
            User user = userService.getUserByUsername(sessionUser.getUserName());
            Path dirPath = Paths.get(AVATAR_DIR);

            // Удаление файла аватара (если это не дефолтный)
            if (user.getAvatarPath() != null && !user.getAvatarPath().equals(DEFAULT_AVATAR)) {
                String fileName = user.getAvatarPath()
                        .replace(AVATAR_URL_PREFIX, "")
                        .split("\\?")[0];
                Files.deleteIfExists(dirPath.resolve(fileName));
            }

            user.setAvatarPath(DEFAULT_AVATAR);
            userService.save(user);
            session.setAttribute("user", user);

            redirect.addFlashAttribute("success", "Аватар удалён");

        } catch (Exception e) {
            e.printStackTrace();
            redirect.addFlashAttribute("error", "Ошибка удаления аватара");
        }

        return "redirect:/profile";
    }
}