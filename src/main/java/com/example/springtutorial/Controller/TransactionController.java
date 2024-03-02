package com.example.springtutorial.Controller;

import com.example.springtutorial.Service.TransactionService;
import com.example.springtutorial.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bank-accounts")
public class TransactionController {

    @Autowired
    private UserService userService;
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<String> withdraw(@PathVariable Long accountId, @RequestParam double amount) {
        try {
            userService.withdraw(accountId, amount);
            return ResponseEntity.ok("Withdraw successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestParam Long fromAccountId, @RequestParam Long toAccountId, @RequestParam double amount) {
        try {
            transactionService.operationTransaction(fromAccountId, toAccountId, amount);
            return ResponseEntity.ok("Transfer successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
