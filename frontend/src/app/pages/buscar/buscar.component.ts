import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MatTabGroup } from '@angular/material/tabs';
import { FiltroConsultaDTO } from 'src/app/_dto/filtroConsultaDTO';
import { ConsultaService } from 'src/app/_service/consulta.service';
import * as moment from 'moment';
import { Consulta } from 'src/app/_model/consulta';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { BuscarDialogoComponent } from './buscar-dialogo/buscar-dialogo.component';

@Component({
  selector: 'app-buscar',
  templateUrl: './buscar.component.html',
  styleUrls: ['./buscar.component.css']
})
export class BuscarComponent implements OnInit {

  form: FormGroup;
  displayedColumns = ['paciente', 'medico', 'especialidad', 'fecha', 'acciones'];
  dataSource: MatTableDataSource<Consulta>;

  @ViewChild('tab') tabGroup: MatTabGroup;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private consultaService: ConsultaService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.form = new FormGroup({
      'dni': new FormControl(''),
      'nombreCompleto': new FormControl(''),
      'fechaConsulta1': new FormControl(),
      'fechaConsulta2': new FormControl(),
    });
  }

  buscar() {
    if (this.tabGroup.selectedIndex == 0) {
      let dni = this.form.value['dni']; //this.form.get('dni').value;
      let nombreCompleto = this.form.value['nombreCompleto']; //this.form.get('nombreCompleto').value;

      let filtro = new FiltroConsultaDTO(dni, nombreCompleto);

      if (filtro.dni.length === 0) {
        delete filtro.dni;
      }

      if (filtro.nombreCompleto.length === 0) {
        delete filtro.nombreCompleto
      }   
      
      this.consultaService.buscarOtros(filtro).subscribe(data => {
        this.crearTabla(data);
      });

    } else {
      let fecha1 = this.form.value['fechaConsulta1'];
      let fecha2 = this.form.value['fechaConsulta2'];

      fecha1 = moment(fecha1).format('YYYY-MM-DDTHH:mm:ss');
      fecha2 = moment(fecha2).format('YYYY-MM-DDTHH:mm:ss');

      this.consultaService.buscarFecha(fecha1, fecha2).subscribe(data => {
        this.crearTabla(data);
      });
    }
  }

  crearTabla(data: Consulta[]) {    
    this.dataSource = new MatTableDataSource(data);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  verDetalle(consulta: Consulta){
    this.dialog.open(BuscarDialogoComponent, {
      width: '750px',
      data: consulta
    });
  }

}
