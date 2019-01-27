package com.input.springboot.backend.apirest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.input.springboot.backend.apirest.models.entity.Articulos;
import com.input.springboot.backend.apirest.models.services.IArticulosService;


@CrossOrigin(origins={"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ArticulosRestController {
	
	@Autowired 
	private IArticulosService articulosService;	
	
	@GetMapping("/articulos")
	public List<Articulos> index(){
		return articulosService.findAll();
	}
	
	@GetMapping("/articulos/page/{page}")
	public Page<Articulos> index(@PathVariable Integer page){
		Pageable pageable = PageRequest.of(page, 4);
		return articulosService.findAllPageable(pageable);
	}

}
