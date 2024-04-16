package com.edu.controller;

//paquetes de hateoas
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.edu.dto.ConsultaDTO;
import com.edu.dto.ConsultaListaExamenDTO;
import com.edu.dto.ConsultaResumenDTO;
import com.edu.dto.FiltroConsultaDTO;
import com.edu.exception.ModeloNotFoundException;
import com.edu.model.Archivo;
import com.edu.model.Consulta;
import com.edu.model.Examen;
import com.edu.service.IArchivoService;
import com.edu.service.IConsultaService;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

	@Autowired
	private IConsultaService service;
	
	//
	@Autowired
	private IArchivoService archsrevice;

	@Autowired
	private ModelMapper mapper;

	// LISTAR
	@GetMapping
	public ResponseEntity<List<ConsultaDTO>> listarr() throws Exception {
		List<ConsultaDTO> lista = service.listar().stream().map(p -> mapper.map(p, ConsultaDTO.class))
				.collect(Collectors.toList());

		return new ResponseEntity<>(lista, HttpStatus.OK);
	}

	// LISTAR X ID
	@GetMapping("/{id}")
	public ResponseEntity<ConsultaDTO> listarPorIdd(@PathVariable("id") Integer id) throws Exception {
		Consulta obj = service.listarPorId(id);

		//
		if (obj == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO : " + id);
		}

		ConsultaDTO dto = mapper.map(obj, ConsultaDTO.class);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	// REGISTRAR

	/*
	 * 
	 * @PostMapping public ResponseEntity<Consulta> registrarr(@Valid @RequestBody
	 * ConsultaDTO dtoRequest) throws Exception { Consulta pac =
	 * mapper.map(dtoRequest, Consulta.class); Consulta obj =
	 * service.registrar(pac); // LocalHost:8080/pacientes/5 URI location =
	 * ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
	 * .buildAndExpand(obj.getIdConsulta()).toUri(); return
	 * ResponseEntity.created(location).build(); }
	 */

	// registrar , transaccion
	@PostMapping
	public ResponseEntity<Void> registrar(@Valid @RequestBody ConsultaListaExamenDTO dto) throws Exception {
		Consulta c = mapper.map(dto.getConsulta(), Consulta.class);

		//Agregar la lista de examenes
		List<Examen> examenes = mapper.map(dto.getLstExamen(), new TypeToken<List<Examen>>() {
		}.getType());

		Consulta obj = service.registrarTransaccional(c, examenes);

		///
		// LocalHost:8080/pacientes/5
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(obj.getIdConsulta()).toUri();
		return ResponseEntity.created(location).build();
	}

	// MODIFICAR
	@PutMapping
	public ResponseEntity<Consulta> modificarr(@RequestBody ConsultaDTO dtoRequest) throws Exception {

		Consulta obj = service.listarPorId(dtoRequest.getIdConsulta());

		//
		if (obj == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO : " + dtoRequest.getIdConsulta());
		}

		Consulta pac = mapper.map(dtoRequest, Consulta.class);
		Consulta paciente = service.modificar(pac);

		return new ResponseEntity<>(obj, HttpStatus.OK);
	}

	// ELIMINAR
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarr(@PathVariable("id") Integer id) throws Exception {

		Consulta obj = service.listarPorId(id);

		//
		if (obj == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO : " + id);
		}

		service.eliminar(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/hateoas/{id}")
	public EntityModel<ConsultaDTO> listarHateoas(@PathVariable("id") Integer id) throws Exception {
		Consulta obj = service.listarPorId(id);

		if (obj == null) {
			throw new ModeloNotFoundException("NO se encontro el id " + id);
		}

		ConsultaDTO dto = mapper.map(obj, ConsultaDTO.class);

		// lista
		EntityModel<ConsultaDTO> recurso = EntityModel.of(dto);

		// atributo
		WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).listarPorIdd(id));
		// WebMvcLinkBuilder link2 = linkTo(methodOn(this.getClass()).listarPorIdd(id));
		recurso.add(link1.withRel("paciente-info"));

		return recurso;
	}
	
	
	//FUNCION DE BUSCAR
	
	@GetMapping("/buscar")
	public ResponseEntity<List<ConsultaDTO>> buscarFecha(@RequestParam(value = "fecha1") String fecha1, @RequestParam(value = "fecha2") String fecha2) throws Exception {
		List<Consulta> consultas = new ArrayList<>();

		consultas = service.buscarFecha(LocalDateTime.parse(fecha1), LocalDateTime.parse(fecha2));
		List<ConsultaDTO> consultasDTO = mapper.map(consultas, new TypeToken<List<ConsultaDTO>>() {}.getType());

		return new ResponseEntity<>(consultasDTO, HttpStatus.OK);
	}
	
	@PostMapping("/buscar/otros")
	public ResponseEntity<List<ConsultaDTO>> buscarOtro(@RequestBody FiltroConsultaDTO filtro) throws Exception {
		List<Consulta> consultas = new ArrayList<>();

		consultas = service.buscar(filtro.getDni(), filtro.getNombreCompleto());
		
		List<ConsultaDTO> consulasDTO = mapper.map(consultas, new TypeToken<List<ConsultaDTO>>() {}.getType());

		return new ResponseEntity<List<ConsultaDTO>>(consulasDTO, HttpStatus.OK);
	}
	
	//listado consultaResumen
	
	@GetMapping(value = "/listarResumen")
	public ResponseEntity<List<ConsultaResumenDTO>> listarResumen() {
		List<ConsultaResumenDTO> consultas = new ArrayList<>();
		consultas = service.listarResumen();
		return new ResponseEntity<List<ConsultaResumenDTO>>(consultas, HttpStatus.OK);
	}
	///
	
	//generar reporte
																///APPLICATION_OCTET_STREAM_VALUE
	@GetMapping(value = "/generarReporte", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE) // APPLICATION_PDF_VALUE
	public ResponseEntity<byte[]> generarReporte() {
		byte[] data = null;
		data = service.generarReporte();
		return new ResponseEntity<>(data, HttpStatus.OK);
	}	
	
	//guardar archivo
	@PostMapping(value = "/guardarArchivo", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<Integer> guardarArchivo(@RequestParam("adjunto") MultipartFile file) throws Exception{

		int rpta = 0;
		
		Archivo ar = new Archivo();
		ar.setFiletype(file.getContentType());
		ar.setFilename(file.getOriginalFilename());
		ar.setValue(file.getBytes());
		
		rpta = archsrevice.guardar(ar);
		
		return new ResponseEntity<Integer>(rpta, HttpStatus.OK);
	}
	
	
	//leerarchivo
	@GetMapping(value = "/leerArchivo/{idArchivo}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> leerArchivo(@PathVariable("idArchivo") Integer idArchivo) throws IOException {

		byte[] arr = archsrevice.leerArchivo(idArchivo);

		return new ResponseEntity<>(arr, HttpStatus.OK);
	}
	
}
