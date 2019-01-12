package com.input.springboot.backend.apirest.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.input.springboot.backend.apirest.models.entity.Cliente;

public interface IClienteService {
	public  List<Cliente> findAll();     		//devuelve todos los resultados
	
	public  Page<Cliente> findAll(Pageable pageable);     		//devuelve todos los resultados paginados
	
	public Cliente findById(Long id);			//bucar un objeto por su id
	
	public Cliente save(Cliente cliente);		//añadir un objeto
	
	public void delete(Long id);				//eliminar un objeto
}

