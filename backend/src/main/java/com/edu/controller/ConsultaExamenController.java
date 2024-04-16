package com.edu.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.dto.ConsultaExamenDTO;
import com.edu.model.ConsultaExamen;
import com.edu.service.IConsultaExamenService;

@RestController
@RequestMapping("/consultaexamenes")
public class ConsultaExamenController {

	@Autowired
	private IConsultaExamenService service;
	
	@Autowired
	private ModelMapper mapper;
	
	//
	@GetMapping(value =  "/{idConsulta}")
	public ResponseEntity<List<ConsultaExamenDTO>> listarex(@PathVariable("idConsulta") Integer idconsulta){
		List<ConsultaExamen> ce = new ArrayList<>();
		
		ce = service.listarExamenesPorConsulta(idconsulta);
		List<ConsultaExamenDTO> ceDTO = mapper.map(ce, new TypeToken<List<ConsultaExamenDTO>> () {}.getType());
		return new ResponseEntity<List<ConsultaExamenDTO>>(ceDTO,HttpStatus.OK);
	}
}
