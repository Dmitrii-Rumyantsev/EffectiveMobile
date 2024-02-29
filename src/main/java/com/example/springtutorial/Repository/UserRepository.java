package com.example.springtutorial.Repository;

import com.example.springtutorial.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByPhones(String phones);
    User findByEmails(String emails);
    List<User> findAllByFullName(String fullname);
    List<User> findAllByDateOfBirth(LocalDate dateOfBirth);
}
