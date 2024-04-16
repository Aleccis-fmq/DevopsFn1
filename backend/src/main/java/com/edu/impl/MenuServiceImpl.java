package com.edu.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.edu.model.Menu;
import com.edu.repo.IMenuRepo;
import com.edu.service.IMenuService;


@Service
public class MenuServiceImpl extends ICRUDServiceImpl<Menu, Integer> implements IMenuService{

	
	@Autowired
	private IMenuRepo iMenuRepo;
	
	

	@Override
	protected JpaRepository<Menu, Integer> getRepo() {
		
		return iMenuRepo;
	}
	
	@Override
	public List<Menu> listarMenuPorUsuario(String nombre) {
		return iMenuRepo.listarMenuPorUsuario(nombre);
	}
}
