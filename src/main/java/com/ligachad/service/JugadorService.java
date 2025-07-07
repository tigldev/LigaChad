package com.ligachad.service;

import com.ligachad.model.Jugador;
import com.ligachad.model.JugadorSuplente;
import com.ligachad.model.JugadorTitular;

import java.util.List;
import java.util.Optional;


public interface JugadorService {
    Jugador registrarJugador(String tipoJugador, String nombre, int edad);
    Optional<Jugador> buscarJugadorPorNombre(String nombre);
    List<Jugador> getAllJugadores();

    void updateMinutosJugados(JugadorTitular jugador, int minutos);
    void updatePartidosIngresadosDesdeBanco(JugadorSuplente jugador, int partidos);
}