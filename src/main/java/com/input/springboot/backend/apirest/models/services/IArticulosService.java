package com.input.springboot.backend.apirest.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.input.springboot.backend.apirest.models.entity.Articulos;

public interface IArticulosService {
	
	public List<Articulos> findAll();
	
	public Page<Articulos> findAllPageable(Pageable pageable);
	
	public Articulos findById(Long id);
	
	public Articulos save(Articulos articulo);
	
	public void delete(Long id);
}
