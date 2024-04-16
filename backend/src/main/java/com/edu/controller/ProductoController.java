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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.edu.dto.ProductoDTO;
import com.edu.exception.ModeloNotFoundException;
import com.edu.model.Medico;
import com.edu.service.IMedicoService;


import io.swagger.annotations.Api;


@RestController
@RequestMapping("/productos")
public class ProductoController {

	@Autowired
	private IMedicoService service;

	@Autowired
	private ModelMapper mapper;
	
	
	
	//autor
	//@PreAuthorize("@authServiceImpl.tieneAcceso('listar')")
	//@PreAuthorize("hasAuthority('ADMIN')")
	// LISTAR
	@GetMapping
	public ResponseEntity<List<ProductoDTO>> listarr() throws Exception {
		List<ProductoDTO> lista = service.listar().stream().map(p -> mapper.map(p, ProductoDTO.class))
				.collect(Collectors.toList());

		return new ResponseEntity<>(lista, HttpStatus.OK);
	}

	// LISTAR X ID
	@GetMapping("/{id}")
	public ResponseEntity<ProductoDTO> listarPorIdd(@PathVariable("id") Integer id) throws Exception {
		Medico obj = service.listarPorId(id);

		//
		if (obj == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO : " + id);
		}

		ProductoDTO dto = mapper.map(obj, ProductoDTO.class);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	// REGISTRAR
	@PostMapping
	public ResponseEntity<Medico> registrarr(@Valid @RequestBody ProductoDTO dtoRequest) throws Exception {
		Medico pac = mapper.map(dtoRequest, Medico.class);
		Medico obj = service.registrar(pac);
		// LocalHost:8080/pacientes/5
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(obj.getIdMedico()).toUri();
		return ResponseEntity.created(location).build();
	}

	// MODIFICAR
	@PutMapping
	public ResponseEntity<Medico> modificarr(@RequestBody ProductoDTO dtoRequest) throws Exception {

		Medico obj = service.listarPorId(dtoRequest.getIdMedico());

		//
		if (obj == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO : " + dtoRequest.getIdMedico());
		}

		Medico pac = mapper.map(dtoRequest, Medico.class);
		Medico paciente = service.modificar(pac);

		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	// ELIMINAR
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarr(@PathVariable("id") Integer id) throws Exception {
		
		Medico obj = service.listarPorId(id);

		//
		if (obj == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO : " + id);
		}
		
		service.eliminar(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/hateoas/{id}")
	public EntityModel<ProductoDTO> listarHateoas(@PathVariable ("id") Integer id) throws Exception{
		Medico obj = service.listarPorId(id);
		
		if (obj == null) {
			throw new ModeloNotFoundException("NO se encontro el id " + id );
		}
		
		ProductoDTO dto = mapper.map(obj, ProductoDTO.class);
		
		//lista
		EntityModel<ProductoDTO> recurso = EntityModel.of(dto);
		
		//atributo
		WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).listarPorIdd(id));
		//WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).listarPorIdd(id));
		recurso.add(link1.withRel("paciente-info"));
		
		return recurso;
	}
}
