package com.example.springtutorial.Model;

import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "transactions")
@Setter
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prevAccountId")
    private Long prevAccountId;

    @Column(name = "toAccountId")
    private Long toAccountId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "complete")
    private boolean flag;

    public Transactions(){}
    public Transactions(Long prevAccountId, Long toAccountId,
                       Double amount, boolean flag){
        this.prevAccountId = prevAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.flag = flag;
    }
}
