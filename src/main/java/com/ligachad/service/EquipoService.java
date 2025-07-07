package com.ligachad.service;

import com.ligachad.model.Equipo;
import com.ligachad.model.Jugador;

import java.util.List;
import java.util.Optional;


public interface EquipoService {
    Equipo crearEquipo(String nombreEquipo);
    Optional<Equipo> buscarEquipoPorNombre(String nombreEquipo);
    List<Equipo> getAllEquipos();
    boolean addJugadorToEquipo(Jugador jugador, Equipo equipo);
    boolean removeJugadorFromEquipo(Jugador jugador, Equipo equipo);
}