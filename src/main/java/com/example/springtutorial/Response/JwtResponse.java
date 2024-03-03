package com.example.springtutorial.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String password;
    private String fullName;

    private Date dateOfBirth;

    private List<String> email;

    private List<String> phones;
    private Double amount;

    public JwtResponse(String token, Long id, String username,
                       String password, String fullName,
                       Date dateOfBirth, List<String> email,
                       List<String> phones, Double amount) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phones = phones;
        this.amount = amount;
    }
}
