package com.example.springtutorial.Service;

import com.example.springtutorial.Model.User;
import com.example.springtutorial.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserService{


    private final double PROCENT = 1.05;
    private final double MAX_PROCENT = 2.07;

    @Autowired
    private UserRepository userRepository;

    public User createUser( String password, String fullName, Date dateOfBirth, String phone, String email, Double amount) {
        boolean isExistingUser = userRepository.findAll().stream()
                .anyMatch(user -> user.getPhones().contains(phone) || user.getEmails().contains(email));

        if (isExistingUser) {
            throw new IllegalArgumentException("Пользователь с таким телефоном или почтой уже существует!");
        }

        User user = new User(password, fullName, dateOfBirth, Collections.singletonList(phone), Collections.singletonList(email), amount);
        return userRepository.save(user);
    }

    public User updateUserContactInfo(Long userId, String type, String addingInfo) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (type.equalsIgnoreCase("phone") || type.equalsIgnoreCase("email")) {
            if ("phone".equalsIgnoreCase(type) && userRepository.findByPhones(addingInfo) == null) {
                List<String> list = user.getPhones();
                list.add(addingInfo);
                user.setPhones(list);
            }
            else if(userRepository.findByEmails(addingInfo) == null){
                List<String> list = user.getEmails();
                list.add(addingInfo);
                user.setEmails(list);
            }
        }
        return userRepository.save(user);
    }

    public void deleteUserContactInfo(Long userId, String contactType,String delete) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if(contactType.equalsIgnoreCase("phone") || contactType.equalsIgnoreCase("email")) {
            if ("phone".equalsIgnoreCase(contactType) && user.getPhones().size() != 1) {
                List<String> phone = user.getPhones();
                phone.remove(delete);
                user.setPhones(phone);
            } else if ("email".equalsIgnoreCase(contactType) && user.getEmails().size() != 1) {
                List<String> emails = user.getEmails();
                emails.remove(delete);
                user.setEmails(emails);
            }
        }
        userRepository.save(user);
    }

    public void withdraw(Long accountId, double amount){
        User user = userRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("ПОЛЬЗОВАТЕЛЬ НЕ НАЙДЕН"));

        double currentBalance = user.getAmount();
        if(currentBalance - amount < 0){
            return;
        }
        user.setAmount(currentBalance - amount);
        userRepository.save(user);
    }

    public User searchUsersByPhone(String phones) {
        return userRepository.findByPhones(phones);
    }

    public User searchUsersByEmails(String emails) {
        return userRepository.findByEmails(emails);
    }

    public List<User> searchUsersByDateOfBirth(LocalDate dateOfBirth) {
        return userRepository.findAllByDateOfBirth(dateOfBirth);
    }

    public List<User> searchUsersByFullName(String fullName) {
        return userRepository.findAllByFullName(fullName);
    }

    public void updateBalances() {
        List<User> user = userRepository.findAll();
        for (User users : user) {
            if (users.getInitianalAmount() * MAX_PROCENT < users.getAmount() * PROCENT) {
                double oldBalance = users.getAmount();
                double newBalance = oldBalance * PROCENT;
                System.out.println(newBalance);
                users.setAmount(newBalance);
                userRepository.save(users);
            }
        }
    }

    public Runnable reload() {
        return this::updateBalances;
    }

}
