package com.input.springboot.backend.apirest.auth;

import java.util.Arrays;

import javax.persistence.criteria.Order;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/clientes", "/api/clientes/page/**",  //permitir a todos los usuarios a nuestra api
				"/api/articulos", "/api/articulos/page/**",
				"/api/usuario", "/api/uploads/img/**",
				"/images/").permitAll()		
		.anyRequest().authenticated()	
		.and().cors().configurationSource(corsConfigurationSource());
		
		/*.antMatchers(HttpMethod.GET, "/api/clientes/{id}").hasAnyRole("USER", "ADMIN")
		.antMatchers(HttpMethod.POST, "/api/clientes/upload", "/api/usuarios/upload").hasAnyRole("USER", "ADMIN")
		.antMatchers(HttpMethod.POST, "/api/clientes","/api/usuario").hasRole("ADMIN")
		.antMatchers("/api/clientes/**","/api/articulos/**", "/api/usuarios/**").hasRole("ADMIN")
		
		asignamos los roles de usuarios que
		deniega todas las rutas que no esten configuradas*/
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));		//Dominios Auth
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));	 //Metodos Auth
		config.setAllowCredentials(true);
		config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization")); 	//Cabeceras perimitidas
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();		//metodos del backend que permitimos
		source.registerCorsConfiguration("/**", config);
		
		return source;
	}
	
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter(){
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		
		return bean;
	}
	
}
