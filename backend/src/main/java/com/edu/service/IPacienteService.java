package com.edu.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.edu.model.Paciente;


public interface IPacienteService extends ICRUD<Paciente, Integer>{
	
	//paginadores
	Page<Paciente> listarPageable(Pageable page);

	
}
