package com.edu.repo;

import com.edu.model.Usuario;

public interface IUsuarioRepo extends IGenericRepo<Usuario, Integer>  {

	//from usuario where username = ?
	//Derived Query
	Usuario findOneByUsername(String username);	
}
