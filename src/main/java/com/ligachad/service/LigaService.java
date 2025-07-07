package com.ligachad.service;

import com.ligachad.model.Equipo;
import com.ligachad.model.Jugador;
import com.ligachad.model.Partido;

import java.util.List;
import java.util.Optional;


public interface LigaService {


    Jugador registrarJugador(String tipoJugador, String nombre, int edad);
    Optional<Jugador> buscarJugadorPorNombre(String nombre);
    void mostrarListadoTodosJugadores();


    Equipo crearEquipo(String nombreEquipo);
    Optional<Equipo> buscarEquipoPorNombre(String nombreEquipo);


    Partido registrarPartido(String nombreEquipoLocal, String nombreEquipoVisitante);
    boolean asignarGolesAJugadorEnPartido(String nombrePartido, String nombreJugador, int goles);


    void mostrarGoleadorDeLaLiga();
    void mostrarPromedioGolesPorEquipo();
    void mostrarRankingEquiposPorGoles();
    void mostrarSuplentesNuncaIngresados();
    void mostrarTitularConMasMinutosJugados();
    void generarReporteGeneralLiga();
    void generarReporteDeEquipo(String nombreEquipo);


    boolean incorporarJugadorAEquipo(String nombreJugador, String nombreEquipo);
    boolean transferirJugador(String nombreJugador, String nombreEquipoOrigen, String nombreEquipoDestino);
    boolean exportarJugadoresEquipoCSV(String nombreEquipo, String nombreArchivo);


    List<Jugador> getJugadores();
    List<Equipo> getEquipos();
    List<Partido> getPartidos();
}