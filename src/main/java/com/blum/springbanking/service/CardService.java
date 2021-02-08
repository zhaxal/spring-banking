package com.blum.springbanking.service;

import com.blum.springbanking.CustomUserDetails;
import com.blum.springbanking.models.Card;
import com.blum.springbanking.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepo;

    @Async
    public CompletableFuture<ArrayList<Card>> getUserCardList(long userId){
        ArrayList<Card> cardList = new ArrayList<>();
        for (Card card : cardRepo.findAll()) {
            if (card.getUser_id() == userId) {
                cardList.add(card);
            }
        }
        return CompletableFuture.completedFuture(cardList);
    }

    @Async
    public CompletableFuture<ArrayList<Card>> getCardList(long userId){
        ArrayList<Card> cardList = new ArrayList<>();
        for (Card card : cardRepo.findAll()) {
            if (card.getUser_id() != userId) {
                cardList.add(card);
            }
        }
        return CompletableFuture.completedFuture(cardList);
    }

    public double getBalance() {
        double balance = 0;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        long userId = user.getId();
        for (Card card : cardRepo.findAll()) {
            if (card.getUser_id() == userId) {
                balance = balance + card.getMoney();
            }
        }
        return balance;
    }
}
