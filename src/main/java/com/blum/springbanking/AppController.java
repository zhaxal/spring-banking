package com.blum.springbanking;

import com.blum.springbanking.models.Card;
import com.blum.springbanking.models.User;
import com.blum.springbanking.repository.CardRepository;
import com.blum.springbanking.repository.UserRepository;
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

@Controller
public class AppController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CardRepository cardRepo;
	
	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}
	
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		
		return "registration";
	}

	@GetMapping("/login")
	public String showRegistrationForm() {
		return "login";
	}

	@GetMapping("/mybank")
	public String showMyBank(Model model) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
		long userId = user.getId();

		double balance = 0;

		ArrayList<Card> cardList = new ArrayList<>();
		for (Card card: cardRepo.findAll()) {
			if (card.getUser_id() == userId){
				cardList.add(card);
				balance = balance + card.getMoney();
			}
			System.out.println(card.toString());
		}

		model.addAttribute("balance",balance);
		model.addAttribute("cardList",cardList);


		return "my-bank";
	}
	@GetMapping("/transfers")
	public String showTransfers(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
		long userId = user.getId();

		ArrayList<Card> cardList = new ArrayList<>();
		ArrayList<Card> allCardList = new ArrayList<>();
		for (Card card: cardRepo.findAll()) {
			if (card.getUser_id() == userId){
				cardList.add(card);
			}
			if (card.getUser_id() != userId){
				allCardList.add(card);
			}
			System.out.println(card.toString());
		}
		model.addAttribute("allList",allCardList);
		model.addAttribute("cardList",cardList);
		return "transfer-page";
	}
	
	@PostMapping("/process_register")
	public String processRegister(User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		userRepo.save(user);

		return "index";
	}

	@GetMapping("/createcard") // Map ONLY POST Requests
	public String addNewCard () {
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
	public String toUserCard(@RequestParam(name="cardfrom") long cardFrom, @RequestParam(name="cardTo") long cardTo, @RequestParam(name="addMoney") double addMoney, Model model) {
		Card from = cardRepo.findCardByCardNumber(cardFrom).get();
		Card to = cardRepo.findCardByCardNumber(cardTo).get();

		cardRepo.delete(to);
		cardRepo.delete(from);

		addMoney = addMoney - addMoney/100;

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
}
