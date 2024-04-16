import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { LoginService } from './login.service';
import { MenuService } from './menu.service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { environment } from 'src/environments/environment';
import { map } from 'rxjs';
import { Menu } from '../_model/menu';
import { count } from 'console';


@Injectable({
  providedIn: 'root'
})
export class GuardService implements CanActivate{

  constructor(
    private loginService : LoginService,
    private menuService : MenuService,
    private router : Router
  ) { }


  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {

    //1 verificar si esta logeado
    let rpta= this.loginService.estaLogeado();
    if(!rpta){
      this.loginService.cerrarSesion();
      return false;
    }

    //2 verificar si el token no ha expirado

    const helper = new JwtHelperService();
    let token = sessionStorage.getItem(environment.TOKEN_NAME)
    if(!helper.isTokenExpired(token)){
      //3 verificar si tienes el rol necesario para acceder

      //url -> pages/paciente
      let url = state.url;

      const decodedtToken = helper.decodeToken(token);

      return this.menuService.listarPorUsuario(decodedtToken.user_name).pipe(map((data : Menu[]) =>{
        this.menuService.setMenuCambio(data);
        
        let cont  = 0 ;
        for(let m of data){
          if (url.startsWith(m.url)){
            cont++;
            break;
          }
        }

        if(cont > 0) {
          return true;
        }else{
          this.router.navigate(['/pages/not-403']);
          return false;
        }


      }));

    }else{
      this.loginService.cerrarSesion();
      return false;
    }
  }
}
