import { Component, OnInit } from '@angular/core';
import { Route, Router } from '@angular/router';
import { Menu } from 'src/app/_model/menu';
import { LoginService } from 'src/app/_service/login.service';
import { MenuService } from 'src/app/_service/menu.service';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.css']
})
export class LayoutComponent implements OnInit {




  menus : Menu[];

  constructor(
    private router : Router,
    //reac
    private menuService : MenuService,

    //funcion cerrar sesion
    private loginService : LoginService

  ) { }

  ngOnInit(): void {

    this.menuService.listarPorUsuario(sessionStorage.getItem('username'))
    .subscribe(data => this.menus = data);


    this.menuService.getMenuCambio().subscribe(data => {
        this.menus = data;
    });
  }


  cerrarSesion(){
    this.loginService.cerrarSesion();
  }
}
