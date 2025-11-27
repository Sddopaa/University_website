package com.Lb5.University_website.models;

import jakarta.persistence.*;

@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "user_id")
public class Admin extends User {

    public Admin() {
        super();
    }

    public Admin(String userName, String password,
                 String firstName, String lastName, String patronymic, int age) {
        super(userName, password, "admin", firstName, lastName, patronymic, age);
    }
}