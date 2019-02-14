package com.input.springboot.backend.apirest.models.services;

import com.input.springboot.backend.apirest.models.entity.Usuario;

public interface IUsuarioService {
	public Usuario findByUsername(String username);
	
	public Usuario save(Usuario usuario);
}
