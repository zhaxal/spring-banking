package com.blum.springbanking.service;

import com.blum.springbanking.CustomUserDetails;
import com.blum.springbanking.models.Authority;
import com.blum.springbanking.models.Role;
import com.blum.springbanking.models.User;
import com.blum.springbanking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}

		Role role = user.getRole();
		Set<Authority> authorities = role.getAuthorities();
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

		for (Authority authority : authorities)
		{
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getName());
			grantedAuthorities.add( grantedAuthority );
		}

		GrantedAuthority roleAuthority = new SimpleGrantedAuthority( role.getName() );
		grantedAuthorities.add( roleAuthority );

		return new CustomUserDetails(user, grantedAuthorities);
	}



}
