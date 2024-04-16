package com.edu.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

public class ConsultaListaExamenDTO {

	@NotNull
	private ConsultaDTO consulta;

	@NotNull
	private List<ObservacionDTO> lstExamen;

	public ConsultaDTO getConsulta() {
		return consulta;
	}

	public void setConsulta(ConsultaDTO consulta) {
		this.consulta = consulta;
	}

	public List<ObservacionDTO> getLstExamen() {
		return lstExamen;
	}

	public void setLstExamen(List<ObservacionDTO> lstExamen) {
		this.lstExamen = lstExamen;
	}

}
