package com.ligachad.service;

import com.ligachad.model.Equipo;
import com.ligachad.model.Jugador;
import com.ligachad.model.Partido;

import java.util.List;
import java.util.Optional;


public interface PartidoService {
    Partido registrarPartido(Equipo equipoLocal, Equipo equipoVisitante);
    boolean asignarGolesAJugadorEnPartido(Partido partido, Jugador goleador, int goles);
    List<Partido> getAllPartidos();
    Optional<Partido> buscarPartidoPorNombre(String nombreEquipo1, String nombreEquipo2);
}