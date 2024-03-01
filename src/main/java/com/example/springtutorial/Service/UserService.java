package com.example.springtutorial.Service;

import com.example.springtutorial.Model.User;
import com.example.springtutorial.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(String username, String password,
                           String fullName, Date dateOfBirth,
                           String phone, String email,
                           Double amount) {

        boolean isExistingUser = userRepository.findAll().stream()
                .anyMatch(user -> user.getPhones().contains(phone) || user.getEmails().contains(email));

        if (isExistingUser) {
            throw new IllegalArgumentException("Пользователь с таким телефоном или почтой уже существует!");
        }

        User user = new User(username, password, fullName,
                dateOfBirth, Collections.singletonList(phone),
                Collections.singletonList(email), amount);
        user.setInitianalAmount(amount);
        return userRepository.save(user);
    }

    public User updateUserContactInfo(Long userId, String type, String addingInfo) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );

        boolean isPhoneType = type.equalsIgnoreCase("phone");
        boolean isEmailType = type.equalsIgnoreCase("email");

        if (isPhoneType || isEmailType) {
            boolean isAddingInfoAbsent = isPhoneType ?
                    userRepository.findByPhones(addingInfo) == null :
                    userRepository.findByEmails(addingInfo) == null;

            if (isAddingInfoAbsent) {
                List<String> list = isPhoneType ? user.getPhones() : user.getEmails();
                list.add(addingInfo);
                if (isPhoneType) {
                    user.setPhones(list);
                } else {
                    user.setEmails(list);
                }
            }
        }

        return userRepository.save(user);
    }

    public void deleteUserContactInfo(Long userId, String contactType, String delete) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );

        if (contactType.equalsIgnoreCase("phone")
                || contactType.equalsIgnoreCase("email")) {
            List<String> listToDeleteFrom = contactType.equalsIgnoreCase("phone") ?
                    user.getPhones() : user.getEmails();

            if (listToDeleteFrom.size() != 1) {
                listToDeleteFrom.removeIf(info -> info.equals(delete));

                if (contactType.equalsIgnoreCase("phone")) {
                    user.setPhones(listToDeleteFrom);
                } else {
                    user.setEmails(listToDeleteFrom);
                }
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
        Date dateOfBirthDate = java.sql.Date.valueOf(dateOfBirth);
        return userRepository.findAllByDateOfBirth(dateOfBirthDate);
    }

    public List<User> searchUsersByFullName(String fullName) {
        return userRepository.findAllByFullName(fullName);
    }

    public void updateBalances() {
        List<User> user = userRepository.findAll();
        for (User users : user) {
            double oldBalance = users.getInitianalAmount() * 2.07;
            double newBalance = users.getAmount() * 1.05;
            if (oldBalance > newBalance) {
                users.setAmount(newBalance);
                userRepository.save(users);
            }
        }
    }

    public Runnable reload() {
        return this::updateBalances;
    }
}
