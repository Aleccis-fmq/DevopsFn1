import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { map, Observable } from 'rxjs';
import { Consulta } from 'src/app/_model/consulta';
import { DetalleConsulta } from 'src/app/_model/detalleConsulta';
import { Especialidad } from 'src/app/_model/especialidad';
import { Examen } from 'src/app/_model/examen';
import { Medico } from 'src/app/_model/medico';
import { Paciente } from 'src/app/_model/paciente';
import { ConsultaService } from 'src/app/_service/consulta.service';
import { EspecialidadService } from 'src/app/_service/especialidad.service';
import { ExamenService } from 'src/app/_service/examen.service';
import { MedicoService } from 'src/app/_service/medico.service';
import { PacienteService } from 'src/app/_service/paciente.service';
import * as moment from 'moment';
import { ConsultaListaExamenDTO } from 'src/app/_dto/consultaListaExamenDTO';

@Component({
  selector: 'app-consulta-especial',
  templateUrl: './consulta-especial.component.html',
  styleUrls: ['./consulta-especial.component.css']
})
export class ConsultaEspecialComponent implements OnInit {

  form: FormGroup;
  pacientes: Paciente[];
  medicos: Medico[];
  especialidades: Especialidad[];
  examenes: Examen[];
  mensaje: string;


  //utiles para el autocomplete
  myControlPaciente: FormControl = new FormControl('', Validators.required);
  myControlMedico: FormControl = new FormControl();

  pacientesFiltrados$: Observable<Paciente[]>;
  medicosFiltrados$: Observable<Medico[]>;

  fechaSeleccionada: Date = new Date();
  maxFecha: Date = new Date();

  pacienteSeleccionado: Paciente;
  medicoSeleccionado: Medico;
  especialidadSeleccionada: Especialidad;
  examenSeleccionado: Examen;

  diagnostico: string;
  tratamiento: string;

  detalleConsulta: DetalleConsulta[] = [];
  examenesSeleccionados: Examen[] = [];

  constructor(
    private consultaService: ConsultaService,
    private medicoService: MedicoService,
    private pacienteService: PacienteService,
    private especialidadService: EspecialidadService,
    private examenService: ExamenService,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.form = new FormGroup({
      'paciente': this.myControlPaciente,
      'medico': this.myControlMedico,
      'especialidad': new FormControl(),
      'fecha': new FormControl(new Date()),
      'diagnostico': new FormControl(),
      'tratamiento': new FormControl()
    });

    this.listarInicial();

    this.pacientesFiltrados$ = this.myControlPaciente.valueChanges.pipe(map(val => this.filtrarPacientes(val)));
    this.medicosFiltrados$ = this.myControlMedico.valueChanges.pipe(map(val => this.filtrarMedicos(val)));
  }

  filtrarPacientes(val: any) {
    return this.pacientes.filter(el =>
      el.nombres.toLowerCase().includes(val) || el.apellidos.toLowerCase().includes(val) || el.dni.includes(val)
    );
  }

  filtrarMedicos(val: any) {
    return this.medicos.filter(el =>
      el.nombres.toLowerCase().includes(val) || el.apellidos.toLowerCase().includes(val) || el.cmp.includes(val)
    );
  }

  mostrarPaciente(val: any) {
    return val ? `${val.nombres} ${val.apellidos}` : val;
  }

  mostrarMedico(val: any) {
    return val ? `${val.nombres} ${val.apellidos}` : val;
  }

  listarInicial() {
    this.pacienteService.listar().subscribe(data => {
      this.pacientes = data;
    });

    this.medicoService.listar().subscribe(data => {
      this.medicos = data;
    });

    this.especialidadService.listar().subscribe(data => {
      this.especialidades = data;
    });

    this.examenService.listar().subscribe(data => {
      this.examenes = data;
    });
  }

  agregar() {

    if (this.diagnostico != null && this.tratamiento != null) {
      let det = new DetalleConsulta();
      det.diagnostico = this.diagnostico;
      det.tratamiento = this.tratamiento;
      this.detalleConsulta.push(det);
      this.diagnostico = null;
      this.tratamiento = null;
    } else {
      this.mensaje = `Debe agregar un diagnóstico y tramiento`;
      this.snackBar.open(this.mensaje, "Aviso", { duration: 2000 });
    }
  }

  agregarExamen() {
    if (this.examenSeleccionado) {
      let cont = 0;
      for (let i = 0; i < this.examenesSeleccionados.length; i++) {
        let examen = this.examenesSeleccionados[i];
        if (examen.idExamen === this.examenSeleccionado.idExamen) {
          cont++;
          break;
        }
      }
      if (cont > 0) {
        this.mensaje = `El examen se encuentra en la lista`;
        this.snackBar.open(this.mensaje, "Aviso", { duration: 2000 });
      } else {
        this.examenesSeleccionados.push(this.examenSeleccionado);
      }
    } else {
      this.mensaje = `Debe agregar un examen`;
      this.snackBar.open(this.mensaje, "Aviso", { duration: 2000 });
    }
  }

  removerDiagnostico(index: number) {
    this.detalleConsulta.splice(index, 1);
  }

  removerExamen(index: number) {
    this.examenesSeleccionados.splice(index, 1);
  }

  aceptar(){
    let consulta = new Consulta();
    consulta.paciente = this.form.value['paciente'];
    consulta.medico = this.form.value['medico'];
    consulta.especialidad = this.form.value['especialidad'];
    consulta.numConsultorio = "C1";
    consulta.fecha = moment(this.form.value['fecha']).format('YYYY-MM-DDTHH:mm:ss');
    consulta.detalleConsulta = this.detalleConsulta;

    let dto = new ConsultaListaExamenDTO();
    dto.consulta = consulta;
    dto.lstExamen = this.examenesSeleccionados;

    this.consultaService.registrarTransaccion(dto).subscribe(() => {
      this.snackBar.open("Se registró", "Aviso", { duration: 2000 });

      setTimeout(() => {
        this.limpiarControles();
      }, 2000)

    });
  }

  limpiarControles() {
    this.form.reset();
    this.detalleConsulta = [];
    this.examenesSeleccionados = [];
  }

}
