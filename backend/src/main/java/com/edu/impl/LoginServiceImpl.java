package com.edu.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.edu.model.Usuario;
import com.edu.repo.ILoginRepo;
import com.edu.service.ILoginService;

@Service
public class LoginServiceImpl implements ILoginService{
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;	
	
	@Autowired
	private ILoginRepo repo;

	@Override
	public Usuario verificarNombreUsuario(String usuario) {
		return repo.verificarNombreUsuario(usuario);
	}

	@Override
	public void cambiarClave(String clave, String nombre) {
		repo.cambiarClave(bcrypt.encode(clave), nombre);
	}
	
	

}
