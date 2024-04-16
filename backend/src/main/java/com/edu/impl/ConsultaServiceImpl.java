package com.edu.impl;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edu.dto.ConsultaResumenDTO;
import com.edu.model.Consulta;
import com.edu.model.Examen;
import com.edu.repo.IConsultaExamenRepo;
import com.edu.repo.IConsultaRepo;
import com.edu.service.IConsultaService;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ConsultaServiceImpl extends ICRUDServiceImpl<Consulta, Integer> implements IConsultaService{

	//
	@Autowired
	private IConsultaRepo rep;

	
	//
	@Autowired
	private IConsultaExamenRepo ceRepo;
	
	@Override
	protected JpaRepository<Consulta, Integer> getRepo() {
		// TODO Auto-generated method stub
		return rep;
	}
	
	

	
	//VISTA PARA EL CONTROLADOR
	
	//Ejecutar el sql desde la forma global
	@Transactional
	@Override
	public Consulta registrarTransaccional(Consulta consulta, List<Examen> examenes) throws Exception {
		consulta.getDetalleConsulta().forEach(det -> det.setConsulta(consulta));
		
		rep.save(consulta);
		
		examenes.forEach(e -> ceRepo.registrar(consulta.getIdConsulta(), e.getIdExamen()));
		return consulta;
	}

	
	//BUSCAR
	

	@Override
	public List<Consulta> buscar(String dni, String nombreCompleto) throws Exception {
		// TODO Auto-generated method stub
		return rep.buscar(dni, nombreCompleto);
	}


	@Override
	public List<Consulta> buscarFecha(LocalDateTime fecha1, LocalDateTime fecha2) throws Exception {
		// TODO Auto-generated method stub
		return rep.buscarFecha(fecha1, fecha2.plusDays(1));
	}

	
	//

	@Override
	public List<ConsultaResumenDTO> listarResumen() {
		// Lista de arreglos
		
		List<ConsultaResumenDTO> consultas =  new ArrayList<>();
		
		rep.listarResumen().forEach(x -> {
			ConsultaResumenDTO cr =  new ConsultaResumenDTO();
			cr.setCantidad(Integer.parseInt(String.valueOf(x[0])));
			cr.setFecha(String.valueOf(x[1]));
			consultas.add(cr);
			
		});
		
		return consultas;
	}


	
	
	//pdf
	@Override
	public byte[] generarReporte() {
		byte[] data = null;
		
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("txt_titulo", "Descarga en PDF");
		
		File file;
		try {
			file = new ClassPathResource("/reportes/consultasf.jasper").getFile();
			JasperPrint print = JasperFillManager.fillReport(file.getPath(), parametros, new JRBeanCollectionDataSource(listarResumen()));
			data = JasperExportManager.exportReportToPdf(print);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return data;
	}


	
	
	
	//
	
	

	
	
	
}
