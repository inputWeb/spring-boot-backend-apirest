package com.input.springboot.backend.apirest.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired					//implementa la clase usuarioService
	private UserDetailsService usuariosServicies;
	
	@Bean				//para poder implementarlo en otras clases
	public BCryptPasswordEncoder passwordEncoder(){			//encripta un passwor de manera segura
		return new BCryptPasswordEncoder();
	}

	@Override
	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usuariosServicies).passwordEncoder(passwordEncoder());
	}
	
	@Bean("authenticationManager")
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.anyRequest().authenticated()		//deniega todas las rutas que no esten configuradas
		.and()
		.csrf().disable()					//desactivamos las cors porque se trabaja desde angular
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);		//desactivamos las sesiones
	}
	
}