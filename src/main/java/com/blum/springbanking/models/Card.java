package com.blum.springbanking.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "userID")
    private int user_id;
    @Column(name = "card_number")
    private long cardNumber;
    @Column(name = "cardCVC")
    private int cardCVC;
    @Column(name = "money")
    private double money;


    public void setId(Long id) {
        this.id = id;
    }

    public void setCardNumber(long card_number) {
        this.cardNumber = card_number;
    }

    public void setCardCVC(int cardCVC) {
        this.cardCVC = cardCVC;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getUser_id() {
        return user_id;
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public int getCardCVC() {
        return cardCVC;
    }

    public double getMoney() {
        return money;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", card_number=" + cardNumber +
                ", cardCVC=" + cardCVC +
                ", money=" + money +
                '}';
    }
}
