package com.Lb5.University_website.repository;

import com.Lb5.University_website.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUserName(String userName);
    List<Student> findByFaculty(String faculty); // Добавляем этот метод
}