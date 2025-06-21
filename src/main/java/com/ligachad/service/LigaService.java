package com.ligachad.service;

import com.ligachad.model.Equipo;
import com.ligachad.model.Jugador;
import com.ligachad.model.JugadorSuplente;
import com.ligachad.model.JugadorTitular;
import com.ligachad.model.Partido;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface LigaService {

    Jugador registrarJugador(String tipoJugador, String nombre, int edad);

    Optional<Jugador> buscarJugadorPorNombre(String nombre);


    Equipo crearEquipo(String nombreEquipo);

    Optional<Equipo> buscarEquipoPorNombre(String nombreEquipo);

    boolean incorporarJugadorAEquipo(String nombreJugador, String nombreEquipo);

    Partido registrarPartido(String nombreEquipoLocal, String nombreEquipoVisitante);

    boolean asignarGolesAJugadorEnPartido(String nombrePartido, String nombreJugador, int goles);

    void mostrarListadoTodosJugadores();

    void mostrarGoleadorDeLaLiga();

    void mostrarPromedioGolesPorEquipo();

    void mostrarRankingEquiposPorGoles();

    boolean transferirJugador(String nombreJugador, String nombreEquipoOrigen, String nombreEquipoDestino);

    void mostrarSuplentesNuncaIngresados();

    void mostrarTitularConMasMinutosJugados();

    boolean exportarJugadoresEquipoCSV(String nombreEquipo, String nombreArchivo);

    void generarReporteGeneralLiga();

    void generarReporteDeEquipo(String nombreEquipo);


    List<Jugador> getJugadores();
    List<Equipo> getEquipos();
    List<Partido> getPartidos();
}