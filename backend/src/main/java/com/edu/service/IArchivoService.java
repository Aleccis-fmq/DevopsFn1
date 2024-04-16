package com.edu.service;

import com.edu.model.Archivo;

public interface IArchivoService {
	
	//int
	int guardar(Archivo archivo);
	
	//byte
	byte[] leerArchivo(Integer idArchivo);
	

}
