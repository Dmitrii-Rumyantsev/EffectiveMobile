package com.example.springtutorial.Controller;

import com.example.springtutorial.Model.User;
import com.example.springtutorial.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private final ScheduledExecutorService scheduledExecutorService = Executors.
            newSingleThreadScheduledExecutor();

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User users = userService.createUser(user.getUsername(),
                user.getPassword(), user.getFullName(),
                user.getDateOfBirth(), user.getPhones().get(0),
                user.getEmails().get(0), user.getAmount());

        return ResponseEntity.status(HttpStatus.CREATED).body(users);
    }

    @PostConstruct
    public void startProcent() {
        int INTERVAL = 1;
        scheduledExecutorService.scheduleAtFixedRate(userService.reload(), 0,
                INTERVAL, TimeUnit.MINUTES);
    }

    @PutMapping("/{userId}/update-contact-info")
    public ResponseEntity<User> updateUserContactInfo(@PathVariable Long userId,
                                                      @RequestBody Map<String, String> request) {
        String type = request.get("type");
        String adding = request.get("adding");
        User user = userService.updateUserContactInfo(userId, type, adding);
        return ResponseEntity.ok(user);
    }


    @DeleteMapping("/{userId}/delete-contact-info")
    public ResponseEntity<Void> deleteUserContactInfo(@PathVariable Long userId,
                                                      @RequestParam String contactType,
                                                      @RequestParam String delete) {
        userService.deleteUserContactInfo(userId, contactType, delete);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(
            @RequestParam(required = false) String dateOfBirth,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email) {

        List<User> users = null;
        System.out.println(dateOfBirth);
        if (dateOfBirth != null) {
            LocalDate dob = LocalDate.parse(dateOfBirth);
            users = userService.searchUsersByDateOfBirth(dob);
        } else if (phone != null) {
            users = Collections.singletonList(userService.searchUsersByPhone(phone));
        } else if (email != null) {
            users = Collections.singletonList(userService.searchUsersByEmails(email));
        } else if (fullName != null) {
            users = userService.searchUsersByFullName(fullName);
        } else {
            throw new IllegalArgumentException("Нет фильтрации по данному запросу");
        }

        return ResponseEntity.ok(users);
    }


}
