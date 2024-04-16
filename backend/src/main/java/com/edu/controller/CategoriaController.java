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

import com.edu.dto.CategoriaDTO;
import com.edu.exception.ModeloNotFoundException;
import com.edu.model.Especialidad;
import com.edu.service.IEspecialidadService;


@RestController
@RequestMapping("/categorias")
public class CategoriaController {

	@Autowired
	private IEspecialidadService service;

	@Autowired
	private ModelMapper mapper;

	// LISTAR
	@GetMapping
	public ResponseEntity<List<CategoriaDTO>> listarr() throws Exception {
		List<CategoriaDTO> lista = service.listar().stream().map(p -> mapper.map(p, CategoriaDTO.class))
				.collect(Collectors.toList());

		return new ResponseEntity<>(lista, HttpStatus.OK);
	}

	// LISTAR X ID
	@GetMapping("/{id}")
	public ResponseEntity<CategoriaDTO> listarPorIdd(@PathVariable("id") Integer id) throws Exception {
		Especialidad obj = service.listarPorId(id);

		//
		if (obj == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO : " + id);
		}

		CategoriaDTO dto = mapper.map(obj, CategoriaDTO.class);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	// REGISTRAR
	@PostMapping
	public ResponseEntity<Especialidad> registrarr(@Valid @RequestBody CategoriaDTO dtoRequest) throws Exception {
		Especialidad pac = mapper.map(dtoRequest, Especialidad.class);
		Especialidad obj = service.registrar(pac);
		// LocalHost:8080/pacientes/5
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(obj.getIdEspecialidad()).toUri();
		return ResponseEntity.created(location).build();
	}

	// MODIFICAR
	@PutMapping
	public ResponseEntity<Especialidad> modificarr(@RequestBody CategoriaDTO dtoRequest) throws Exception {

		Especialidad obj = service.listarPorId(dtoRequest.getIdEspecialidad());

		//
		if (obj == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO : " + dtoRequest.getIdEspecialidad());
		}

		Especialidad pac = mapper.map(dtoRequest, Especialidad.class);
		Especialidad paciente = service.modificar(pac);

		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	// ELIMINAR
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarr(@PathVariable("id") Integer id) throws Exception {
		
		Especialidad obj = service.listarPorId(id);

		//
		if (obj == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO : " + id);
		}
		
		service.eliminar(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/hateoas/{id}")
	public EntityModel<CategoriaDTO> listarHateoas(@PathVariable ("id") Integer id) throws Exception{
		Especialidad obj = service.listarPorId(id);
		
		if (obj == null) {
			throw new ModeloNotFoundException("NO se encontro el id " + id );
		}
		
		CategoriaDTO dto = mapper.map(obj, CategoriaDTO.class);
		
		//lista
		EntityModel<CategoriaDTO> recurso = EntityModel.of(dto);
		
		//atributo
		WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).listarPorIdd(id));
		//WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).listarPorIdd(id));
		recurso.add(link1.withRel("paciente-info"));
		
		return recurso;
	}
}
