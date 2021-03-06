package com.blum.springbanking.controllers;

import com.blum.springbanking.CustomUserDetails;
import com.blum.springbanking.models.Card;
import com.blum.springbanking.models.User;
import com.blum.springbanking.repository.CardRepository;
import com.blum.springbanking.repository.UserRepository;
import com.blum.springbanking.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

@Controller
public class AppController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CardRepository cardRepo;

	@Autowired
	private CardService cardService;

	Logger logger = LoggerFactory.getLogger(AppController.class);
	
	@GetMapping("")
	public String viewHomePage() {
		logger.info("Index page visited");
		return "index";
	}
	
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		logger.info("Registration page visited");
		return "registration";
	}

	@GetMapping("/login")
	public String showRegistrationForm() {
		logger.info("Login page visited");
		return "login";
	}

	@GetMapping("/mybank")
	public String showMyBank(Model model) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
			long userId = user.getId();
			double balance = cardService.getBalance();
			ArrayList<Card> cardList = cardService.getUserCardList(userId).get();
			model.addAttribute("balance", balance);
			model.addAttribute("cardList", cardList);
			logger.info("My bank page visited");
			logger.info("User balance loaded");
			logger.info("User cards loaded");
		} catch (Exception e){
			logger.error(e.getMessage(),e);
		}




		return "my-bank";
	}
	@GetMapping("/transfers")
	public String showTransfers(Model model) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
			long userId = user.getId();

			ArrayList<Card> cardList = cardService.getUserCardList(userId).get();
			ArrayList<Card> allCardList = cardService.getCardList(userId).get();

			model.addAttribute("allList", allCardList);
			model.addAttribute("cardList", cardList);
			logger.info("Transfers page visited");
			logger.info("User cards loaded");
		} catch (Exception e){
			logger.error(e.getMessage(),e);
		}
		return "transfer-page";
	}
	
	@PostMapping("/process_register")
	public String processRegister(User user) {
		try{
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		userRepo.save(user);
		logger.info("");
		} catch (Exception e){
			logger.error(e.getMessage(),e);
		}
		return "index";
	}




	@GetMapping("/topup")
	public String topUp(@RequestParam(name="cardNumber") long cardNumber, @RequestParam(name="addMoney") double addMoney, Model model) {
		double moneyOn = cardRepo.findCardByCardNumber(cardNumber).get().getMoney();
		moneyOn = moneyOn + addMoney;
		Card card = cardRepo.findCardByCardNumber(cardNumber).get();
		card.setMoney(moneyOn);
		cardRepo.delete(card);
		cardRepo.save(card);
		return "index";
	}

	@GetMapping("/tomycard")
	public String toMyCard(@RequestParam(name="cardfrom") long cardFrom, @RequestParam(name="cardTo") long cardTo, @RequestParam(name="addMoney") double addMoney, Model model) {
		Card from = cardRepo.findCardByCardNumber(cardFrom).get();
		Card to = cardRepo.findCardByCardNumber(cardTo).get();

		cardRepo.delete(to);
		cardRepo.delete(from);

		double fromint = from.getMoney()-addMoney;
		double tint = to.getMoney()+addMoney;

		from.setMoney(fromint);
		to.setMoney(tint);

		System.out.println(from.toString());
		System.out.println(to.toString());

		cardRepo.save(to);
		cardRepo.save(from);
		return "index";
	}


	@GetMapping("/tousercard")
	public String toUserCard(@RequestParam(name="cardfrom") long cardFrom, @RequestParam(name="addMoney") double addMoney, Model model) {
		Card from = cardRepo.findCardByCardNumber(cardFrom).get();

		cardRepo.delete(from);

		addMoney = addMoney - addMoney/100;

		double fromint = from.getMoney()-addMoney;

		from.setMoney(fromint);

		System.out.println(from.toString());
		cardRepo.save(from);
		return "index";
	}

	@GetMapping("/services")
	public String services(Model model) throws ExecutionException, InterruptedException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
		long userId = user.getId();

		ArrayList<Card> cardList = cardService.getUserCardList(userId).get();

		model.addAttribute("cardList",cardList);

		return "services";
	}

	@GetMapping("/payment")
	public String payment(@RequestParam(name="cardBy") long cardBy, @RequestParam(name="service") String service, Model model) {
		Card by = cardRepo.findCardByCardNumber(cardBy).get();

		cardRepo.delete(by);
		double payment = 0;
		payment = payment + by.getMoney();
		if (service == "debt"){
			payment = payment - 10000;
		}
		if (service == "comunalka"){
			payment = payment - 5000;
		}
		by.setMoney(payment);

		System.out.println(by.toString());
		cardRepo.save(by);
		return "index";
	}
}
