package com.ligachad.service.impl;

import com.ligachad.model.Equipo;
import com.ligachad.model.Jugador;
import com.ligachad.service.EquipoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class EquipoServiceImpl implements EquipoService {

    private List<Equipo> equipos;

    public EquipoServiceImpl() {
        this.equipos = new ArrayList<>();
    }

    @Override
    public Equipo crearEquipo(String nombreEquipo) {
        if (buscarEquipoPorNombre(nombreEquipo).isPresent()) {
            System.out.println("Error: Ya existe un equipo con el nombre '" + nombreEquipo + "'.");
            return null;
        }
        Equipo nuevoEquipo = new Equipo(nombreEquipo);
        equipos.add(nuevoEquipo);
        System.out.println("Equipo '" + nombreEquipo + "' creado exitosamente.");
        return nuevoEquipo;
    }

    @Override
    public Optional<Equipo> buscarEquipoPorNombre(String nombreEquipo) {
        return equipos.stream()
                .filter(e -> e.getNombre().equalsIgnoreCase(nombreEquipo))
                .findFirst();
    }

    @Override
    public List<Equipo> getAllEquipos() {
        return new ArrayList<>(equipos);
    }

    @Override
    public boolean addJugadorToEquipo(Jugador jugador, Equipo equipo) {
        if (jugador == null || equipo == null) {
            System.out.println("Error: Jugador o equipo nulo.");
            return false;
        }
        if (jugador.getEquipo() != null && jugador.getEquipo().equals(equipo)) {
            System.out.println("Error: El jugador '" + jugador.getNombre() + "' ya pertenece a este equipo.");
            return false;
        }
        if (jugador.getEquipo() != null) {
            System.out.println("Advertencia: El jugador '" + jugador.getNombre() + "' ya pertenece a otro equipo. Se le quitará de ese equipo.");
            jugador.getEquipo().quitarJugador(jugador);
        }
        equipo.agregarJugador(jugador);
        return true;
    }

    @Override
    public boolean removeJugadorFromEquipo(Jugador jugador, Equipo equipo) {
        if (jugador == null || equipo == null) {
            System.out.println("Error: Jugador o equipo nulo.");
            return false;
        }
        if (!equipo.getJugadores().contains(jugador)) {
            System.out.println("Error: El jugador '" + jugador.getNombre() + "' no pertenece a este equipo.");
            return false;
        }
        return equipo.quitarJugador(jugador);
    }
}