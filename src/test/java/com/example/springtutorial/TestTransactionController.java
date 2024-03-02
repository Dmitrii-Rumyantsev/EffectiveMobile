package com.example.springtutorial;

import com.example.springtutorial.Model.User;
import com.example.springtutorial.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annoпtation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestTransactionController {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    @BeforeEach
    void setUp() {
        user1 = new User("username1", "password1", "Full Name 1", new Date(), List.of("12345"), List.of("email1@example.com"), 1000.0);
        user2 = new User("username2", "password2", "Full Name 2", new Date(), List.of("54321"), List.of("email2@example.com"), 2000.0);

        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    void testTransfer() {
        Long fromAccountId = user1.getId();
        Long toAccountId = user2.getId();
        Double amount = 100.0;

        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "/bank-accounts/transfer?fromAccountId={fromAccountId}&toAccountId={toAccountId}&amount={amount}",
                null, String.class, fromAccountId, toAccountId, amount);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Перевод денег совершен", response.getBody());
    }

    @Test
    void testTransferInsufficientFunds() {
        Long fromAccountId = user1.getId();
        Long toAccountId = user2.getId();
        Double amount = 1000.0;

        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "http://localhost:" + port + "/bank-accounts/transfer?fromAccountId={fromAccountId}&toAccountId={toAccountId}&amount={amount}",
                null, String.class, fromAccountId, toAccountId, amount);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("У вас недостаточно средств для перевода", response.getBody());
    }
}
