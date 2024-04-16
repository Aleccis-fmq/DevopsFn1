package com.edu.service;

import java.util.List;

import com.edu.model.ConsultaExamen;

public interface IConsultaExamenService {
	List<ConsultaExamen> listarExamenesPorConsulta(Integer idconsulta);
}
