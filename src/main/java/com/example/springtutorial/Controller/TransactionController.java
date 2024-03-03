package com.example.springtutorial.Controller;

import com.example.springtutorial.Security.Service.UserDetailsImpl;
import com.example.springtutorial.Security.Service.UserDetailsServiceImpl;
import com.example.springtutorial.Service.TransactionService;
import com.example.springtutorial.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bank-accounts")
public class TransactionController {

    @Autowired
    private UserService userService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserDetailsServiceImpl userDetails;

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestParam double amount, Authentication authentication) {
        try {
            String username = authentication.getName();
            UserDetailsImpl users = userDetails.loadUserByUsername(username);
            userService.withdraw(users.getId(), amount);
            return ResponseEntity.ok("Withdraw successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestParam Long toAccountId, @RequestParam double amount, Authentication authentication) {
        try {
            String username = authentication.getName();
            UserDetailsImpl users = userDetails.loadUserByUsername(username);
            transactionService.operationTransaction(users.getId(), toAccountId, amount);
            return ResponseEntity.ok("Transfer successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
