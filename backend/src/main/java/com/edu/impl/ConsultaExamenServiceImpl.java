package com.edu.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.model.ConsultaExamen;
import com.edu.repo.IConsultaExamenRepo;
import com.edu.service.IConsultaExamenService;


@Service
public class ConsultaExamenServiceImpl implements IConsultaExamenService {

	
	@Autowired
	private IConsultaExamenRepo rep ;
	
	@Override
	public List<ConsultaExamen> listarExamenesPorConsulta(Integer idconsulta) {
		
		return rep.listarExamenesPorConsulta(idconsulta);
	}

}
