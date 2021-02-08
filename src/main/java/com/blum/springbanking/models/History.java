package com.blum.springbanking.models;

import javax.persistence.*;

@Entity
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "action")
    private String action;
    @Column(name = "user_id")
    private Long user_id;
    @Column(name = "money")
    private double money;





    public void setMoney(double money) {
        this.money = money;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public double getMoney() {
        return money;
    }

    public Long getUser_id() {
        return user_id;
    }

    public String getAction() {
        return action;
    }

    @Override
    public String toString() {
        return "History{" +
                "id=" + id +
                ", action='" + action + '\'' +
                ", user_id=" + user_id +
                ", money=" + money +
                '}';
    }
}
