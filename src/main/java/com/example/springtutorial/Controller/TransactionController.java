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
    public ResponseEntity<String> withdraw(@PathVariable Long accountId,
                                           @RequestParam Double amount) {
        try {
            userService.withdraw(accountId, amount);
            return ResponseEntity.ok("Вы сняли деньги");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Недостаточно средств");
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestParam Long fromAccountId,
                                           @RequestParam Long toAccountId,
                                           @RequestParam Double amount) {
        try {
            transactionService.operationTransaction(fromAccountId, toAccountId, amount);
            return ResponseEntity.ok("Перевод денег совершен");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("У вас недостаточно средств для перевода");
        }
    }
}
