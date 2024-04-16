import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { ConsultaListaExamenDTO } from 'src/app/_dto/consultaListaExamenDTO';
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
import { MatSnackBar } from '@angular/material/snack-bar';

/*interface consultaListaExamenDTO{
  consulta: Consulta;
  lstExamen: Examen[];
}*/

@Component({
  selector: 'app-consulta',
  templateUrl: './consulta.component.html',
  styleUrls: ['./consulta.component.css']
})
export class ConsultaComponent implements OnInit {

  pacientes$: Observable<Paciente[]>;
  medicos$: Observable<Medico[]>;
  examenes$: Observable<Examen[]>;
  especialidades$: Observable<Especialidad[]>;

  maxFecha: Date = new Date();
  diagnostico: string;
  tratamiento: string;

  detalleConsulta: DetalleConsulta[] = [];
  examenesSeleccionados: Examen[] = [];

  idPacienteSeleccionado: number;
  idMedicoSeleccionado: number;
  idEspecialidadSeleccionado: number;
  idExamenSeleccionado: number;
  fechaSeleccionada: Date = new Date();

  constructor(
    private pacienteService: PacienteService,
    private medicoService: MedicoService,
    private examenService: ExamenService,
    private especialidadService: EspecialidadService,
    private consultaService: ConsultaService,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.listarPacientes();
    this.listarMedicos();
    this.listarExamenes();
    this.listarEspecialidades();
  }

  agregar() {
    let det = new DetalleConsulta();
    det.diagnostico = this.diagnostico;
    det.tratamiento = this.tratamiento;

    this.detalleConsulta.push(det);
  }

  agregarExamen() {
    if (this.idExamenSeleccionado > 0) {

      this.examenService.listarPorId(this.idExamenSeleccionado).subscribe(data => {
        this.examenesSeleccionados.push(data);
      });

    }
  }

  aceptar() {
    let medico = new Medico();
    medico.idMedico = this.idMedicoSeleccionado;

    let paciente = new Paciente();
    paciente.idPaciente = this.idPacienteSeleccionado;

    let especialidad = new Especialidad();
    especialidad.idEspecialidad = this.idEspecialidadSeleccionado;

    let consulta = new Consulta();
    consulta.medico = medico;
    consulta.paciente = paciente;
    consulta.especialidad = especialidad;
    consulta.numConsultorio = "C1";
    consulta.detalleConsulta = this.detalleConsulta;

    /*let tzoffset = (new Date()).getTimezoneOffset() * 60000;
    let localISOTime = (new Date(this.fechaSeleccionada.getTime() - tzoffset)).toISOString();*/

    consulta.fecha = moment(this.fechaSeleccionada).format('YYYY-MM-DDTHH:mm:ss');

    let dto = new ConsultaListaExamenDTO();
    dto.consulta = consulta;
    dto.lstExamen = this.examenesSeleccionados;

    this.consultaService.registrarTransaccion(dto).subscribe(data => {
      this.snackBar.open("SE REGISTRO", 'AVISO', { duration: 2000 });

      setTimeout(() => {
        this.limpiarControles();
      }, 2000)
    });
  }

  limpiarControles(){
    this.idPacienteSeleccionado = 0;
    this.idMedicoSeleccionado = 0;
    this.idEspecialidadSeleccionado = 0;
    this.idExamenSeleccionado = 0;
    this.fechaSeleccionada = new Date();
    this.diagnostico = null;
    this.tratamiento = null;
    this.detalleConsulta = [];
    this.examenesSeleccionados = [];
  }

  removerDiagnostico(index: number) {
    this.detalleConsulta.splice(index, 1);
  }

  removerExamen(index: number) {
    this.examenesSeleccionados.splice(index, 1);
  }

  listarPacientes() {
    this.pacientes$ = this.pacienteService.listar();
  }

  listarMedicos() {
    this.medicos$ = this.medicoService.listar();
  }

  listarExamenes() {
    this.examenes$ = this.examenService.listar();
  }

  listarEspecialidades() {
    this.especialidades$ = this.especialidadService.listar();
  }


}
