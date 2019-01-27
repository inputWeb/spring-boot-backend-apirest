package com.input.springboot.backend.apirest.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.input.springboot.backend.apirest.models.dao.IArticulosDao;
import com.input.springboot.backend.apirest.models.entity.Articulos;

@Service
public class ArticulosService implements IArticulosService{
	
	@Autowired
	private IArticulosDao articulosDao;

	@Override
	@Transactional(readOnly = true)
	public List<Articulos> findAll() {
		return (List<Articulos>)articulosDao.findAll();
	}

	@Override
	public Page<Articulos> findAllPageable(Pageable pageable) {
		return articulosDao.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Articulos findById(Long id) {
		return articulosDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Articulos save(Articulos articulo) {
		return articulosDao.save(articulo);
	}

	@Override
	public void delete(Long id) {
		articulosDao.deleteById(id);	
	}

}
