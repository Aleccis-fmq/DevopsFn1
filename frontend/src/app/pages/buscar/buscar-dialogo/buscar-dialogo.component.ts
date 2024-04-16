import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ConsultaListaExamenDTO } from 'src/app/_dto/consultaListaExamenDTO';
import { Consulta } from 'src/app/_model/consulta';
import { ConsultaService } from 'src/app/_service/consulta.service';

@Component({
  selector: 'app-buscar-dialogo',
  templateUrl: './buscar-dialogo.component.html',
  styleUrls: ['./buscar-dialogo.component.css']
})
export class BuscarDialogoComponent implements OnInit {

  consulta: Consulta;
  examenes: any[];

  constructor(
    private dialogRef: MatDialogRef<BuscarDialogoComponent>,
    @Inject(MAT_DIALOG_DATA) private data: Consulta,
    private consultaService: ConsultaService
  ) { }

  ngOnInit(): void {
    this.consulta = { ...this.data };
    this.consultaService.listarExamenPorConsulta(this.consulta.idConsulta).subscribe(data => {
      //[
      //{consulta, examen},
      //{consulta, examen},
      //{consulta, examen}
      // ]

      //[
      // {consulta, examen[]}
      //]
      this.examenes = data;
    });
  }

  cerrar() {
    this.dialogRef.close();
  }
}
