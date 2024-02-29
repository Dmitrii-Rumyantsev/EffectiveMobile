package com.example.springtutorial.Service;

import com.example.springtutorial.Model.Transactions;
import com.example.springtutorial.Model.User;
import com.example.springtutorial.Repository.TransactionRepository;
import com.example.springtutorial.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class TransactionService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public synchronized void operationTransaction(Long fromAccountId, Long toAccountId, double amount){
        User fromAccount = userRepository.findById(fromAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found"));

        User toAccount = userRepository.findById(toAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found"));

        double nowBalance = fromAccount.getAmount();

        if (nowBalance - amount < 0){
            throw new IllegalArgumentException("Insufficient funds to withdraw");
        }

        toAccount.setAmount(toAccount.getAmount() + amount);
        userRepository.save(toAccount);

        fromAccount.setAmount(nowBalance - amount);
        userRepository.save(fromAccount);

        Transactions newTransactional = new Transactions(fromAccount.getId(),
                toAccount.getId(),amount,true);
        transactionRepository.save(newTransactional);
    }

}
