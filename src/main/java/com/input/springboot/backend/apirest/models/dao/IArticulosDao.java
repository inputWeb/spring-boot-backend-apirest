package com.input.springboot.backend.apirest.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.input.springboot.backend.apirest.models.entity.Articulos;

public interface IArticulosDao extends JpaRepository<Articulos, Long> {

}
