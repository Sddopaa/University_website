package com.Lb5.University_website.models;

import jakarta.persistence.*;

/**
 * Модель администратора системы
 */
@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "user_id") // Связь по первичному ключу с таблицей users
public class Admin extends User {

    public Admin() { super(); }

    /**
     * Конструктор с параметрами для создания администратора
     *
     * @param userName    Логин администратора
     * @param password    Пароль администратора
     * @param firstName   Имя администратора
     * @param lastName    Фамилия администратора
     * @param patronymic  Отчество администратора
     * @param age         Возраст администратора
     */
    public Admin(String userName, String password,
                 String firstName, String lastName, String patronymic, int age) {
        // Вызов конструктора родительского класса с фиксированной ролью "admin"
        super(userName, password, "admin", firstName, lastName, patronymic, age);
    }
}