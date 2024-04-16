package com.edu.dto;

public class ConsultaExamenDTO {

	private ConsultaDTO consulta;
	private ObservacionDTO examen;

	public ConsultaDTO getConsulta() {
		return consulta;
	}

	public void setConsulta(ConsultaDTO consulta) {
		this.consulta = consulta;
	}

	public ObservacionDTO getExamen() {
		return examen;
	}

	public void setExamen(ObservacionDTO examen) {
		this.examen = examen;
	}

}
