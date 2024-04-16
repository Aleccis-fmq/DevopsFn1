package com.edu.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.edu.model.Especialidad;
import com.edu.repo.IEspecialidadRepo;
import com.edu.service.IEspecialidadService;

@Service
public class EspecialidadServiceImpl extends ICRUDServiceImpl<Especialidad, Integer> implements IEspecialidadService{

	
	//
	@Autowired
	private IEspecialidadRepo rep;

	@Override
	protected JpaRepository<Especialidad, Integer> getRepo() {
		return rep;
	}
	
	
	

}
