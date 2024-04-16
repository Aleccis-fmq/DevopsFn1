package com.edu.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotNull;

public class ConsultaDTO {

	private Integer idConsulta;
	
	@NotNull
	private ProveedorDTO paciente;
	@NotNull
	private ProductoDTO medico;
	@NotNull
	private CategoriaDTO especialidad;
	@NotNull
	private String numConsultorio;

	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@NotNull
	private LocalDateTime fecha;
	
	@NotNull
	private List<DetalleConsultaDTO> detalleConsulta;
	
	
	//

	public Integer getIdConsulta() {
		return idConsulta;
	}

	public void setIdConsulta(Integer idConsulta) {
		this.idConsulta = idConsulta;
	}

	public ProveedorDTO getPaciente() {
		return paciente;
	}

	public void setPaciente(ProveedorDTO paciente) {
		this.paciente = paciente;
	}

	public ProductoDTO getMedico() {
		return medico;
	}

	public void setMedico(ProductoDTO medico) {
		this.medico = medico;
	}

	public CategoriaDTO getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(CategoriaDTO especialidad) {
		this.especialidad = especialidad;
	}

	public String getNumConsultorio() {
		return numConsultorio;
	}

	public void setNumConsultorio(String numConsultorio) {
		this.numConsultorio = numConsultorio;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public List<DetalleConsultaDTO> getDetalleConsulta() {
		return detalleConsulta;
	}

	public void setDetalleConsulta(List<DetalleConsultaDTO> detalleConsulta) {
		this.detalleConsulta = detalleConsulta;
	}

}
