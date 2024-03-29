package com.example.springtutorial.Service;

import com.example.springtutorial.Model.User;
import com.example.springtutorial.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@EnableScheduling
public class UserService{


    private final double PROCENT = 1.05;
    private final double MAX_PROCENT = 2.07;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;
    public User createUser(User user) {
        boolean isExistingUser = userRepository.findAll().stream()
                .anyMatch(users -> user.getPhones().contains(user.getPhones()) || user.getEmails().contains(user.getEmails()));
        if (isExistingUser) {
            throw new IllegalArgumentException("Пользователь с таким телефоном или почтой уже существует!");
        }
        user.setInitialAmount(user.getAmount());
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUserContactInfo(Long userId, String type, String addingInfo) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        boolean isPhoneType = type.equalsIgnoreCase("phone");
        boolean isEmailType = type.equalsIgnoreCase("email");

        if (isPhoneType || isEmailType) {
            boolean isAddingInfoAbsent = isPhoneType ? userRepository.findByPhones(addingInfo) == null : userRepository.findByEmails(addingInfo) == null;

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
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (contactType.equalsIgnoreCase("phone") || contactType.equalsIgnoreCase("email")) {
            List<String> listToDeleteFrom = contactType.equalsIgnoreCase("phone") ? user.getPhones() : user.getEmails();

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
        return userRepository.findAllByDateOfBirth(dateOfBirth);
    }

    public List<User> searchUsersByFullName(String fullName) {
        return userRepository.findAllByFullName(fullName);
    }

    @Transactional
    @Scheduled(fixedRate = 60_000)
    public void updateBalances() {
        List<User> user = userRepository.findAll();
        for (User users : user) {
            double oldBalance = users.getInitialAmount();
            double newBalance = users.getAmount() * PROCENT;
            if (oldBalance * MAX_PROCENT > newBalance) {
                users.setAmount(newBalance);
                userRepository.save(users);
            }
        }
    }
}
