package com.example.springtutorial.Controller;

import com.example.springtutorial.Model.User;
import com.example.springtutorial.Security.Service.UserDetailsImpl;
import com.example.springtutorial.Security.Service.UserDetailsServiceImpl;
import com.example.springtutorial.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetails;

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User users = userService.createUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(users);
    }
    @PutMapping("/update-contact-info")
    public ResponseEntity<User> updateUserContactInfo(@RequestBody Map<String, String> request, Authentication authentication) {
        String type = request.get("type");
        String adding = request.get("adding");
        String username = authentication.getName();
        UserDetailsImpl users = userDetails.loadUserByUsername(username);
        User user = userService.updateUserContactInfo(users.getId(), type, adding);
        return ResponseEntity.ok(user);
    }


    @DeleteMapping("/delete-contact-info")
    public ResponseEntity<Void> deleteUserContactInfo(@RequestParam String contactType, @RequestParam String delete, Authentication authentication) {
        String username = authentication.getName();
        UserDetailsImpl users = userDetails.loadUserByUsername(username);
        userService.deleteUserContactInfo(users.getId(), contactType, delete);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(
            @RequestParam(required = false) String dateOfBirth,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email) {

        List<User> users = null;

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
            throw new IllegalArgumentException("No Filter");
        }

        return ResponseEntity.ok(users);
    }



}
