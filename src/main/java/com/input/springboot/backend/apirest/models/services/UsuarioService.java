package com.input.springboot.backend.apirest.models.services;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.input.springboot.backend.apirest.models.dao.IUsuarioDao;
import com.input.springboot.backend.apirest.models.entity.Usuario;

@Service 
public class UsuarioService implements IUsuarioService , UserDetailsService{
	
	private Logger logger = LoggerFactory.getLogger(UsuarioService.class);		//permite crear logs
	
	@Autowired
	private IUsuarioDao usuarioDao;
	
	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioDao.findByUsername(username);	//Recuperamos el usuario que queramos comprobar
		
		if(usuario == null) {
			logger.error("Error en el login: no existe el usuario '"+ username +"' en el sistema");		//log de error guardado
			throw new UsernameNotFoundException("Error en el login: no existe el usuario '"+ username +"' en el sistema"); //hacemos saltar la excepcion con el error
		}else {
			
		}
		
		List<GrantedAuthority> authorities = usuario.getRoles()		//obtenemos todos los roles del usuario
				.stream()		//separamos los roles en bloques
				.map(role -> new SimpleGrantedAuthority(role.getNombre())) //hay que buscar los permisos del usuario pero hay que maperlo al objeto GrantedAuthority
				.peek(authority -> logger.info("Role: " + authority.getAuthority())) // ver el nombre del role en la consola
				.collect(Collectors.toList());			//hay que convertilo tmb a una lista para que coincida con los requerimientos
		
				
		
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
	}

	@Override
	@Transactional(readOnly=true)
	public Usuario findByUsername(String username) {
		
		return usuarioDao.findByUsername(username);
	}

}
