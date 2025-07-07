package com.ligachad.service.impl;

import com.ligachad.model.Jugador;
import com.ligachad.model.JugadorSuplente;
import com.ligachad.model.JugadorTitular;
import com.ligachad.service.JugadorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class JugadorServiceImpl implements JugadorService {

    private List<Jugador> jugadores;

    public JugadorServiceImpl() {
        this.jugadores = new ArrayList<>();
    }

    @Override
    public Jugador registrarJugador(String tipoJugador, String nombre, int edad) {
        if (buscarJugadorPorNombre(nombre).isPresent()) {
            System.out.println("Error: Ya existe un jugador con el nombre '" + nombre + "'.");
            return null;
        }

        Jugador nuevoJugador;
        if ("titular".equalsIgnoreCase(tipoJugador)) {
            nuevoJugador = new JugadorTitular(nombre, edad);
        } else if ("suplente".equalsIgnoreCase(tipoJugador)) {
            nuevoJugador = new JugadorSuplente(nombre, edad);
        } else {
            System.out.println("Tipo de jugador inválido. Use 'titular' o 'suplente'.");
            return null;
        }
        jugadores.add(nuevoJugador);
        System.out.println("Jugador '" + nombre + "' registrado como " + tipoJugador + ".");
        return nuevoJugador;
    }

    @Override
    public Optional<Jugador> buscarJugadorPorNombre(String nombre) {
        return jugadores.stream()
                .filter(j -> j.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

    @Override
    public List<Jugador> getAllJugadores() {
        return new ArrayList<>(jugadores); // Retorna una copia
    }

    @Override
    public void updateMinutosJugados(JugadorTitular jugador, int minutos) {
        if (jugador != null && minutos > 0) {
            jugador.setMinutosJugados(jugador.getMinutosJugados() + minutos);
            System.out.println("Minutos actualizados para " + jugador.getNombre() + ": " + jugador.getMinutosJugados());
        }
    }

    @Override
    public void updatePartidosIngresadosDesdeBanco(JugadorSuplente jugador, int partidos) {
        if (jugador != null && partidos > 0) {
            jugador.setPartidosIngresadosDesdeBanco(jugador.getPartidosIngresadosDesdeBanco() + partidos);
            System.out.println("Partidos ingresados actualizados para " + jugador.getNombre() + ": " + jugador.getPartidosIngresadosDesdeBanco());
        }
    }
}