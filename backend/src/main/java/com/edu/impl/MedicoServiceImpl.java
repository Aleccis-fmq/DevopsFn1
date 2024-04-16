package com.edu.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.edu.model.Medico;
import com.edu.repo.IMedicoRepo;
import com.edu.repo.IPacienteRepo;
import com.edu.service.IMedicoService;


@Service
public class MedicoServiceImpl extends ICRUDServiceImpl<Medico, Integer> implements IMedicoService{

	//impl
	@Autowired
	private IMedicoRepo rep;
	
	//abs
	
	
	@Override
	protected JpaRepository<Medico, Integer> getRepo() {
		return rep;
	}

}
