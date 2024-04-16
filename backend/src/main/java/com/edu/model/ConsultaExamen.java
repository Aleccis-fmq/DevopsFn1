package com.edu.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;


@Entity
@IdClass(ConsultaExamenPK.class)
public class ConsultaExamen {

	
	@Id
	private Consulta consulta;
	
	
	@Id
	private Examen examen;


	public Consulta getConsulta() {
		return consulta;
	}


	public void setConsulta(Consulta consulta) {
		this.consulta = consulta;
	}


	public Examen getExamen() {
		return examen;
	}


	public void setExamen(Examen examen) {
		this.examen = examen;
	}
	
	
	
}
