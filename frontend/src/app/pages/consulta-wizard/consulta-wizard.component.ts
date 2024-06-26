import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatStepper } from '@angular/material/stepper';
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

@Component({
  selector: 'app-consulta-wizard',
  templateUrl: './consulta-wizard.component.html',
  styleUrls: ['./consulta-wizard.component.css']
})
export class ConsultaWizardComponent implements OnInit {

  isLinear: boolean;
  primerFormGroup: FormGroup;
  segundoFormGroup: FormGroup;

  pacientes: Paciente[];
  especialidades: Especialidad[];
  medicos: Medico[];
  examenes: Examen[];

  medicoSeleccionado: Medico;
  especialidadSeleccionada: Especialidad;
  pacienteSeleccionado: Paciente;
  examenSeleccionado: Examen;

  fechaSeleccionada: Date = new Date();
  maxFecha: Date = new Date();

  diagnostico: string;
  tratamiento: string;
  mensaje: string;

  detalleConsulta: DetalleConsulta[] = [];
  examenesSeleccionados: Examen[] = [];

  consultorios: number[] = [];
  consultorioSeleccionado: number;

  @ViewChild('stepper') stepper: MatStepper;

  constructor(
    private pacienteService: PacienteService,
    private especialidadService: EspecialidadService,
    private medicoService: MedicoService,
    private examenService: ExamenService,
    private consultaService: ConsultaService,
    private snackBar: MatSnackBar,
    private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    this.primerFormGroup = this.formBuilder.group({
      cboPaciente: [new FormControl(), Validators.required],
      fecha: [new FormControl(new Date()), Validators.required],
      'diagnostico': new FormControl(),
      'tratamiento': new FormControl()
    });

    this.segundoFormGroup = this.formBuilder.group({});

    this.listarInicial();
    this.listarConsultorios();
  }

  listarConsultorios() {
    for (let i = 1; i <= 100; i++) {
      this.consultorios.push(i);
    }
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
      this.mensaje = `Debe agregar un diagnóstico y tratamiento`;
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

  seleccionarPaciente(e: any) {
    this.pacienteSeleccionado = e.value;
  }

  seleccionarEspecialidad(e: any) {
    this.especialidadSeleccionada = e.value;
  }

  seleccionarMedico(medico: Medico) {
    this.medicoSeleccionado = medico;
  }

  seleccionarConsultorio(c: number) {
    this.consultorioSeleccionado = c;
  }

  removerDiagnostico(index: number) {
    this.detalleConsulta.splice(index, 1);
  }

  removerExamen(index: number) {
    this.examenesSeleccionados.splice(index, 1);
  }

  nextManualStep() {
    if (this.consultorioSeleccionado > 0) {              
      //https://stackoverflow.com/questions/47219903/angular-material-prevent-mat-stepper-from-navigating-between-steps    
      this.stepper.selected.completed = true;
      this.stepper.next();
    } else {
      this.snackBar.open('DEBES SELECCIONAR ASIENTO', 'INFO', { duration: 2000 });
    }
  }

  aceptar() {
    let consulta = new Consulta();
    consulta.especialidad = this.especialidadSeleccionada;
    consulta.medico = this.medicoSeleccionado;
    consulta.paciente = this.pacienteSeleccionado;
    consulta.fecha = moment(this.fechaSeleccionada).format('YYYY-MM-DDTHH:mm:ss');
    consulta.detalleConsulta = this.detalleConsulta;
    consulta.numConsultorio = `C${this.consultorioSeleccionado}`;

    let dto = new ConsultaListaExamenDTO();
    dto.consulta = consulta;
    dto.lstExamen = this.examenesSeleccionados;

    this.consultaService.registrarTransaccion(dto).subscribe(() => {
      this.snackBar.open("Se registró", "Aviso", { duration: 2000 });

      setTimeout(() => {
        this.limpiarControles();
      }, 2000);
    });
  }

  limpiarControles() {
    this.primerFormGroup.reset();
    this.segundoFormGroup.reset();
    this.detalleConsulta = [];
    this.examenesSeleccionados = [];
    this.consultorioSeleccionado = 0;
    this.stepper.reset();
    this.diagnostico = '';
    this.tratamiento = '';
    this.mensaje = '';
  }

}
