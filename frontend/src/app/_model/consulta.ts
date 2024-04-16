import { DetalleConsulta } from "./detalleConsulta";
import { Especialidad } from "./especialidad";
import { Medico } from "./medico";
import { Paciente } from "./paciente";

export class Consulta{
    idConsulta: number;
    paciente: Paciente;
    medico: Medico;
    especialidad: Especialidad;
    fecha: string; //ISODate yyyy-mm-ddTHH:mm:ss
    numConsultorio: string;
    detalleConsulta: DetalleConsulta[];
}