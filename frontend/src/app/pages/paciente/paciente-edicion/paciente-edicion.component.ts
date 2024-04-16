import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap } from 'rxjs';
import { Paciente } from 'src/app/_model/paciente';
import { PacienteService } from 'src/app/_service/paciente.service';

@Component({
  selector: 'app-paciente-edicion',
  templateUrl: './paciente-edicion.component.html',
  styleUrls: ['./paciente-edicion.component.css']
})
export class PacienteEdicionComponent implements OnInit {

  form: FormGroup;
  id: number;
  edicion: boolean;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private pacienteService: PacienteService
  ) { }

  ngOnInit(): void {
    this.form = new FormGroup({
      'id': new FormControl(0),
      'nombres': new FormControl('', [Validators.required, Validators.minLength(3)]),
      'apellidos': new FormControl('', [Validators.required, Validators.minLength(3)]),
      'dni': new FormControl(''),
      'direccion': new FormControl(''),
      'telefono': new FormControl(''),
      'email': new FormControl('')
    });

    this.route.params.subscribe(data => {
      this.id = data['id'];
      this.edicion = data['id'] != null;
      this.initForm();
    });
  }  

  get f() { return this.form.controls; }

  initForm(){
    if(this.edicion){
      //MODIFICAR
      this.pacienteService.listarPorId(this.id).subscribe(data => {
        this.form = new FormGroup({
          'id': new FormControl(data.idPaciente),
          'nombres': new FormControl(data.nombres, Validators.required),
          'apellidos': new FormControl(data.apellidos, [Validators.required, Validators.minLength(3)]),
          'dni': new FormControl(data.dni),
          'direccion': new FormControl(data.direccion),
          'telefono': new FormControl(data.telefono),
          'email': new FormControl(data.email)
        });
      });     
    }
  }

  operar(){
    if(this.form.invalid){ return; }

    let paciente = new Paciente();
    paciente.idPaciente = this.form.value['id'];
    paciente.nombres = this.form.value['nombres'];
    paciente.apellidos = this.form.value['apellidos'];
    paciente.dni = this.form.value['dni'];
    paciente.direccion = this.form.value['direccion'];
    paciente.telefono = this.form.value['telefono'];
    paciente.email = this.form.value['email'];

    if(this.edicion){
      //PRACTICA COMUN
      this.pacienteService.modificar(paciente).subscribe(() => {
        this.pacienteService.listar().subscribe(data => {
          this.pacienteService.setPacienteCambio(data);
          this.pacienteService.setMensajeCambio('Se modificó');
        });
      });
    }else{
      //PRACTICA IDEAL
      this.pacienteService.registrar(paciente).pipe(switchMap( () => {
        return this.pacienteService.listar();        
      }))      
      .subscribe(data => {
        this.pacienteService.setPacienteCambio(data);
        this.pacienteService.setMensajeCambio('Se registró');
      });
    }

    this.router.navigate(['/pages/proveedor']);
  }

}
