package com.ligachad.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Equipo {
    private String nombre;
    private List<Jugador> jugadores;

    public Equipo(String nombre) {
        this.nombre = nombre;
        this.jugadores = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }


    public void agregarJugador(Jugador jugador) {
        if (jugador != null && !jugadores.contains(jugador)) {
            this.jugadores.add(jugador);
            jugador.setEquipo(this);
        }
    }


    public boolean quitarJugador(Jugador jugador) {
        if (jugadores.remove(jugador)) {
            jugador.setEquipo(null);
            return true;
        }
        return false;
    }


    public Optional<Jugador> getJugadorPorNombre(String nombreJugador) {
        return jugadores.stream()
                .filter(j -> j.getNombre().equalsIgnoreCase(nombreJugador))
                .findFirst();
    }

    @Override
    public String toString() {
        return "Equipo: " + nombre + " (Jugadores: " + jugadores.size() + ")";
    }
}