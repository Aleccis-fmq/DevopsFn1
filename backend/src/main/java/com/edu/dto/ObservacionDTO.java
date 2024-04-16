package com.edu.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "observacionDTO")
public class ObservacionDTO {

	private Integer idExamen;
	private String nombre;
	private String descripcion;

	public Integer getIdExamen() {
		return idExamen;
	}

	public void setIdExamen(Integer idExamen) {
		this.idExamen = idExamen;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
