package com.edu.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.edu.model.ConsultaExamen;

public interface IConsultaExamenRepo extends IGenericRepo<ConsultaExamen, Integer>{

	
	@Modifying
	@Query(value = "INSERT INTO consulta_examen(id_consulta, id_examen) VALUES (:idConsulta , :idExamen)",
			nativeQuery =true)
	
	Integer registrar(@Param("idConsulta")Integer idConsulta,@Param("idExamen")Integer idExamen);
	
	
	//query adicional
	@Query("FROM ConsultaExamen ce where ce.consulta.idConsulta = :idConsulta")
	List<ConsultaExamen> listarExamenesPorConsulta(@Param("idConsulta") Integer idConsulta);
	
}
