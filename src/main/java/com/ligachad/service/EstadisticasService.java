package com.ligachad.service;

import com.ligachad.model.Equipo;
import com.ligachad.model.Jugador;
import com.ligachad.model.JugadorSuplente;
import com.ligachad.model.JugadorTitular;
import com.ligachad.model.Partido;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface EstadisticasService {
    Optional<Jugador> getGoleadorDeLaLiga(List<Jugador> jugadores);
    Map<Equipo, Double> getPromedioGolesPorEquipo(List<Equipo> equipos, List<Partido> partidos);
    Map<Equipo, Integer> getRankingEquiposPorGoles(List<Equipo> equipos, List<Partido> partidos);
    List<JugadorSuplente> getSuplentesNuncaIngresados(List<Jugador> jugadores);
    Optional<JugadorTitular> getTitularConMasMinutosJugados(List<Jugador> jugadores);
    void generarReporteGeneralLiga(List<Jugador> jugadores, List<Equipo> equipos, List<Partido> partidos);
    void generarReporteDeEquipo(Equipo equipo, List<Partido> partidos);
}