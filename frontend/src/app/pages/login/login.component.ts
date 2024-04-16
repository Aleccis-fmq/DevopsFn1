import { Component, OnInit } from '@angular/core';
import '../../../assets/login-animation.js';
import { LoginService } from 'src/app/_service/login.service';
import { environment } from 'src/environments/environment';
import { Router } from '@angular/router';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  //variables
  usuario : string;
  clave : string;
  mensaje : string;


  constructor(
    private logingService : LoginService,
    private router : Router
  ) { }

  ngOnInit(): void {
  }

  iniciarSesion(){
    this.logingService.login(this.usuario,this.clave).subscribe(data =>{
      //
      sessionStorage.setItem(environment.TOKEN_NAME, data.access_token);
      this.router.navigate(['/pages/inicio']);

    });
  }

  //
  ngAfterViewInit() {
    const initialize = (window as any).initialize;
    if (initialize && typeof initialize === 'function') {
      initialize();
    } else {
      console.error('La función initialize no está definida');
      
    }
  }

  
}
