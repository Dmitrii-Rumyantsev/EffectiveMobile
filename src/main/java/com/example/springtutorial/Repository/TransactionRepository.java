package com.example.springtutorial.Repository;

import com.example.springtutorial.Model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transactions,Long> {
}
