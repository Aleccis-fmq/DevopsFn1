package com.edu.service;

import java.time.LocalDateTime;
import java.util.List;

import com.edu.dto.ConsultaResumenDTO;
import com.edu.model.Consulta;
import com.edu.model.Examen;


public interface IConsultaService extends ICRUD<Consulta, Integer>{

	//AGREGAMOS UNA NUEVA VISTA PARA EJECTUAR EN EL CONTROLADOR
	
	Consulta registrarTransaccional(Consulta consulta , List<Examen> examenes)throws Exception;
	
	// buscar
	
	List<Consulta> buscar(String dni ,String nombreCompleto) throws Exception;
	
	List<Consulta> buscarFecha (LocalDateTime fecha1 , LocalDateTime fecha2 )throws Exception;
	
	//listar resumen , consultaResumenDTO
	
	List<ConsultaResumenDTO> listarResumen();
	
	//pdf
	
	byte[] generarReporte();
	
	
	
}
