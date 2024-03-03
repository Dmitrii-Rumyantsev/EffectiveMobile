package com.example.springtutorial.Model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "fullname", nullable = false)
    private String fullName;

    @Column(name = "dateofbirth", nullable = false)
    private Date dateOfBirth;

    @Column(name = "amount",nullable = false)
    private Double amount;

    @ElementCollection
    @CollectionTable(name = "user_phones", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "phone", nullable = false, unique = true)
    private List<String> phones = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "user_emails", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "email", nullable = false, unique = true)
    private List<String> emails = new ArrayList<>();

    private transient List<Transactions> transactions;

    @Transient
    private Double initianalAmount = 0.0;
    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }


    public User() {
    }

    public User(String username, String password,
                String fullName, Date dateOfBirth,
                List<String> phones, List<String> emails,
                Double amount) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.phones = phones;
        this.emails = emails;
        this.amount = amount;
        this.initianalAmount = amount;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", phone='" + phones + '\'' +
                ", email='" + emails + '\'' +
                ", amount='" + amount + '\'' +
                ", transaction=" + transactions +
                '}';
    }
}



