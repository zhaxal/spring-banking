package com.blum.springbanking.controllers;

import com.blum.springbanking.CustomUserDetails;
import com.blum.springbanking.models.Card;
import com.blum.springbanking.repository.CardRepository;
import com.blum.springbanking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class RestfulController {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CardRepository cardRepo;


    @GetMapping("/createcard") // Map ONLY POST Requests
    public void addNewCard () {
        long leftLimit = 100000000000L;
        long rightLimit = 999999999999L;
        long cardNumber = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
        cardNumber = cardNumber + 5530000000000000L;

        Random rand = new Random();
        int cvc = rand.nextInt(999);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        long userId = user.getId();


        Card card = new Card();
        card.setCardCVC(cvc);
        card.setCardNumber(cardNumber);
        card.setMoney(0);
        card.setUser_id((int) userId);
        System.out.println(card.toString());

        cardRepo.save(card);

    }


}
