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

import com.edu.dto.ObservacionDTO;
import com.edu.exception.ModeloNotFoundException;
import com.edu.model.Examen;
import com.edu.service.IExamenService;


@RestController
@RequestMapping("/observaciones")
public class ObservacionController {

	@Autowired
	private IExamenService service;

	@Autowired
	private ModelMapper mapper;

	// LISTAR
	@GetMapping
	public ResponseEntity<List<ObservacionDTO>> listarr() throws Exception {
		List<ObservacionDTO> lista = service.listar().stream().map(p -> mapper.map(p, ObservacionDTO.class))
				.collect(Collectors.toList());

		return new ResponseEntity<>(lista, HttpStatus.OK);
	}

	// LISTAR X ID
	@GetMapping("/{id}")
	public ResponseEntity<ObservacionDTO> listarPorIdd(@PathVariable("id") Integer id) throws Exception {
		Examen obj = service.listarPorId(id);

		//
		if (obj == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO : " + id);
		}

		ObservacionDTO dto = mapper.map(obj, ObservacionDTO.class);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	// REGISTRAR
	@PostMapping
	public ResponseEntity<Examen> registrarr(@Valid @RequestBody ObservacionDTO dtoRequest) throws Exception {
		Examen pac = mapper.map(dtoRequest, Examen.class);
		Examen obj = service.registrar(pac);
		// LocalHost:8080/pacientes/5
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(obj.getIdExamen()).toUri();
		return ResponseEntity.created(location).build();
	}

	// MODIFICAR
	@PutMapping
	public ResponseEntity<Examen> modificarr(@RequestBody ObservacionDTO dtoRequest) throws Exception {

		Examen obj = service.listarPorId(dtoRequest.getIdExamen());

		//
		if (obj == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO : " + dtoRequest.getIdExamen());
		}

		Examen pac = mapper.map(dtoRequest, Examen.class);
		Examen paciente = service.modificar(pac);

		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	// ELIMINAR
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarr(@PathVariable("id") Integer id) throws Exception {
		
		Examen obj = service.listarPorId(id);

		//
		if (obj == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO : " + id);
		}
		
		service.eliminar(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/hateoas/{id}")
	public EntityModel<ObservacionDTO> listarHateoas(@PathVariable ("id") Integer id) throws Exception{
		Examen obj = service.listarPorId(id);
		
		if (obj == null) {
			throw new ModeloNotFoundException("NO se encontro el id " + id );
		}
		
		ObservacionDTO dto = mapper.map(obj, ObservacionDTO.class);
		
		//lista
		EntityModel<ObservacionDTO> recurso = EntityModel.of(dto);
		
		//atributo
		WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).listarPorIdd(id));
		//WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).listarPorIdd(id));
		recurso.add(link1.withRel("paciente-info"));
		
		return recurso;
	}
}
