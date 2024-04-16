package com.edu.service;

import java.util.List;

import com.edu.model.Menu;

public interface IMenuService extends ICRUD<Menu, Integer>{

	List<Menu> listarMenuPorUsuario(String nombre);
}
