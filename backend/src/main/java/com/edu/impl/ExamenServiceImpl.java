package com.edu.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.edu.model.Examen;
import com.edu.model.Paciente;
import com.edu.repo.IExamenRepo;
import com.edu.repo.IMedicoRepo;
import com.edu.service.IExamenService;

@Service
public class ExamenServiceImpl extends ICRUDServiceImpl<Examen, Integer> implements IExamenService{

	//
	@Autowired
	private IExamenRepo rep;

	@Override
	protected JpaRepository<Examen, Integer> getRepo() {
		return rep;
	}
	
	
	

	
	
	
}
