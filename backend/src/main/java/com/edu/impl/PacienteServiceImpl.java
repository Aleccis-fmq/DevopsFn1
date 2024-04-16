package com.edu.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.edu.model.Paciente;
import com.edu.repo.IPacienteRepo;
import com.edu.service.IPacienteService;

@Service
public class PacienteServiceImpl extends ICRUDServiceImpl<Paciente, Integer> implements IPacienteService{

	
	@Autowired
	private IPacienteRepo rep;
	
	@Override
	protected JpaRepository<Paciente, Integer> getRepo(){
		return rep;
	}

	
	//paginadores
	
	@Override
	public Page<Paciente> listarPageable(Pageable page) {
		return rep.findAll(page);
	}

}
