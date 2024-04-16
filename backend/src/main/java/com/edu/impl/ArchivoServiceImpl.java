package com.edu.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.model.Archivo;
import com.edu.repo.IArchivoRepo;
import com.edu.service.IArchivoService;

@Service
public class ArchivoServiceImpl implements IArchivoService{

	//repo
	
	@Autowired
	private IArchivoRepo archivoRepo;
	
	//nombre extraido
	@Override
	public int guardar(Archivo archivo) {
		
		Archivo ar =  archivoRepo.save(archivo);
		return ar.getIdArchivo() > 0 ? 1 : 0;
	}

	//dato extraido
	@Override
	public byte[] leerArchivo(Integer idArchivo) {
		Optional<Archivo> op = archivoRepo.findById(idArchivo);
		return op.isPresent() ? op.get().getValue() : new byte[0];
	}

}
