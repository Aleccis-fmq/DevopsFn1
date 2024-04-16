package com.edu.service;

import com.edu.model.Usuario;

public interface ILoginService {

	Usuario verificarNombreUsuario(String usuario);
	void cambiarClave(String clave, String nombre);
}
