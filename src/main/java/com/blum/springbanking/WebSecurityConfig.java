package com.blum.springbanking;

import javax.sql.DataSource;

import com.blum.springbanking.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private DataSource dataSource;

	@Bean
	public CustomUserDetailsService userDetailsService(){
		return new CustomUserDetailsService();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/").authenticated()
				.antMatchers("/mybank").hasAnyRole("USER", "ADMIN")
				.antMatchers("/transfers").hasAnyRole("USER", "ADMIN")
				.antMatchers("/topup").hasAnyRole("USER", "ADMIN")
				.antMatchers("/tomycard").hasAnyRole("USER", "ADMIN")
				.antMatchers("/tousercard").hasAnyRole("USER", "ADMIN")
				.antMatchers("/services").hasAnyRole("USER", "ADMIN")
				.antMatchers("/payment").hasAnyRole("USER", "ADMIN")
				.antMatchers("/mybank").hasAnyRole("USER", "ADMIN")
			.and()
			.formLogin()
				.usernameParameter("email")
				.loginPage("/login")
				.defaultSuccessUrl("/")
				.permitAll()
			.and().logout().logoutUrl("/logout").and()
			.logout().logoutSuccessUrl("/").permitAll();
	}
	
	
}
