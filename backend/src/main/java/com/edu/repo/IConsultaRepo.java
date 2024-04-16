package com.edu.repo;

import java.time.LocalDateTime;
import java.util.List;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.edu.model.Consulta;


public interface IConsultaRepo extends IGenericRepo<Consulta, Integer>{

	
	
	//JPQL Java Persistence Query Language
	
	@Query("FROM Consulta c WHERE c.paciente.dni = :dni OR LOWER(c.paciente.nombres) LIKE %:nombreCompleto% OR LOWER(c.paciente.apellidos) LIKE %:nombreCompleto%")
	List<Consulta> buscar(@Param("dni") String dni, String nombreCompleto);
	
	
	// DATE -  DATE
	// >=    -    <
	@Query("FROM Consulta c WHERE c.fecha BETWEEN :fechaConsulta1  AND  :fechaConsulta2")
	List<Consulta> buscarFecha(@Param("fechaConsulta1") LocalDateTime fechaConsulta1, @Param("fechaConsulta2") LocalDateTime fechaConsulta2);

	// >=19-04-2022  <25-04-2022
	
	//procedimiento almacenado cantidad / fecha
	
	@Query(value = "select * from fn_listarResumen()", nativeQuery =  true)
	List<Object[]> listarResumen();
	
	//2	"06/03/2024"
	//2	"07/03/2024"
	//1	"08/03/2024"
	//3	"11/03/2024"
	//6	"29/01/2022"
	
}
