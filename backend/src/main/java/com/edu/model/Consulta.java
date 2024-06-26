package com.edu.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Consulta {

	//Desarrollo JPQL
	
	
	//
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idConsulta;
	
	@ManyToOne
	@JoinColumn(name = "id_paciente",nullable = false,foreignKey = @ForeignKey(name = "FK_consulta_paciente"))
	private Paciente paciente;
	
	@ManyToOne
	@JoinColumn(name = "id_medico",nullable = false,foreignKey = @ForeignKey(name = "FK_consulta_medico"))
	private Medico medico;
	
	@ManyToOne
	@JoinColumn(name = "id_especialidad",nullable = false,foreignKey = @ForeignKey(name = "FK_consulta_especialidad"))
	private Especialidad especialidad;
	
	@Column(length = 3)
	private String numConsultorio;
	
	@Column(nullable = false)
	private LocalDateTime fecha;
	
	//para visualizar el detalleconsulta de cada consulta
	//#
	@OneToMany(mappedBy = "consulta",cascade = {CascadeType.ALL},orphanRemoval = true)
	private List<DetalleConsulta> detalleConsulta;

	
	
	public Integer getIdConsulta() {
		return idConsulta;
	}

	public void setIdConsulta(Integer idConsulta) {
		this.idConsulta = idConsulta;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Medico getMedico() {
		return medico;
	}

	public void setMedico(Medico medico) {
		this.medico = medico;
	}

	public Especialidad getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(Especialidad especialidad) {
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

	public List<DetalleConsulta> getDetalleConsulta() {
		return detalleConsulta;
	}

	public void setDetalleConsulta(List<DetalleConsulta> detalleConsulta) {
		this.detalleConsulta = detalleConsulta;
	}

	
	
	//creacion de tercera
	
	@Override
	public int hashCode() {
		return Objects.hash(idConsulta);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Consulta other = (Consulta) obj;
		return Objects.equals(idConsulta, other.idConsulta);
	}
	
	
}
