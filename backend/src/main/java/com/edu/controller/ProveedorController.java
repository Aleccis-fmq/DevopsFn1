package com.edu.controller;

//paquetes de hateoas
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.edu.dto.ProveedorDTO;
import com.edu.exception.ModeloNotFoundException;
import com.edu.model.Paciente;
import com.edu.service.IPacienteService;

import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
@RequestMapping("/proveedores")


//CORS EXPORTAR EL ACCESO
//@CrossOrigin(origins = "*")

public class ProveedorController {

	@Autowired
	private IPacienteService service;

	@Autowired
	private ModelMapper mapper;

	// LISTAR
	@GetMapping
	public ResponseEntity<List<ProveedorDTO>> listarr() throws Exception {
		List<ProveedorDTO> lista = service.listar().stream().map(p -> mapper.map(p, ProveedorDTO.class))
				.collect(Collectors.toList());

		return new ResponseEntity<>(lista, HttpStatus.OK);
	}

	// LISTAR X ID
	@GetMapping("/{id}")
	public ResponseEntity<ProveedorDTO> listarPorIdd(@PathVariable("id") Integer id) throws Exception {
		Paciente obj = service.listarPorId(id);

		//
		if (obj == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO : " + id);
		}

		ProveedorDTO dto = mapper.map(obj, ProveedorDTO.class);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	// REGISTRAR
	@PostMapping
	public ResponseEntity<Paciente> registrarr(@Valid @RequestBody ProveedorDTO dtoRequest) throws Exception {
		Paciente pac = mapper.map(dtoRequest, Paciente.class);
		Paciente obj = service.registrar(pac);
		// LocalHost:8080/pacientes/5
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(obj.getIdPaciente()).toUri();
		return ResponseEntity.created(location).build();
	}

	// MODIFICAR
	@PutMapping
	public ResponseEntity<Paciente> modificarr(@RequestBody ProveedorDTO dtoRequest) throws Exception {

		Paciente obj = service.listarPorId(dtoRequest.getIdPaciente());

		//
		if (obj == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO : " + dtoRequest.getIdPaciente());
		}

		Paciente pac = mapper.map(dtoRequest, Paciente.class);
		Paciente paciente = service.modificar(pac);

		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	// ELIMINAR
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarr(@PathVariable("id") Integer id) throws Exception {
		
		Paciente obj = service.listarPorId(id);

		//
		if (obj == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO : " + id);
		}
		
		service.eliminar(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/hateoas/{id}")
	public EntityModel<ProveedorDTO> listarHateoas(@PathVariable ("id") Integer id) throws Exception{
		Paciente obj = service.listarPorId(id);
		
		if (obj == null) {
			throw new ModeloNotFoundException("NO se encontro el id " + id );
		}
		
		ProveedorDTO dto = mapper.map(obj, ProveedorDTO.class);
		
		//lista
		EntityModel<ProveedorDTO> recurso = EntityModel.of(dto);
		
		//atributo
		WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).listarPorIdd(id));
		//WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).listarPorIdd(id));
		recurso.add(link1.withRel("paciente-info"));
		
		return recurso;
	}
	
	
	//paginadores
	@GetMapping("/pageable")
	public ResponseEntity<Page<ProveedorDTO>> listarPageable(Pageable page) throws Exception{
		Page<ProveedorDTO> pacientes = service.listarPageable(page).map(p -> mapper.map(p, ProveedorDTO.class));
		
		return new ResponseEntity<>(pacientes, HttpStatus.OK);
	}
	
	
}
