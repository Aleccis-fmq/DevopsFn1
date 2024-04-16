package com.edu.dto;



import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "ProveedorDTO")
public class ProveedorDTO {

	private Integer idPaciente;

	@Schema(description = "Nombres del paciente")
	@NotNull
	
	@Size(min = 3, message = "{nombres.size}")
	//@JsonProperty(value = "nombres_completos")
	private String nombres;

	@Schema(description = "Apellidos del paciente")
	@NotNull
	@Size(min = 3, message = "{apellidos.size}")
	private String apellidos;

	@NotNull
	@Size(min = 8)
	private String dni;

	@NotNull
	@Size(min = 3, max = 150)
	private String direccion;

	@NotEmpty
	@Email(message = "{paciente_email.size}")
	private String email;

	@Size(min = 9, max = 9, message = "{paciente_telefono.size}")
	// @Pattern(regexp = "")
	private String telefono;

	public Integer getIdPaciente() {
		return idPaciente;
	}

	public void setIdPaciente(Integer idPaciente) {
		this.idPaciente = idPaciente;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
