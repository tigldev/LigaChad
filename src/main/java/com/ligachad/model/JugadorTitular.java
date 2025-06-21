package com.ligachad.model;

public class JugadorTitular extends Jugador {
    private int minutosJugados;

    public JugadorTitular(String nombre, int edad) {
        super(nombre, edad);
        this.minutosJugados = 0;
    }

    public int getMinutosJugados() {
        return minutosJugados;
    }

    public void setMinutosJugados(int minutosJugados) {
        this.minutosJugados = minutosJugados;
    }


    @Override
    public void incrementarGoles(int goles) {
        this.cantidadGoles += goles;
    }


    @Override
    public String obtenerTipoJugador() {
        return "Titular";
    }

    @Override
    public String toString() {
        return super.toString() + ", Minutos Jugados: " + minutosJugados + " (Titular)";
    }
}