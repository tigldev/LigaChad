package com.ligachad.model;


public class JugadorSuplente extends Jugador {
    private int partidosIngresadosDesdeBanco;
    private boolean haIngresadoAlgunaVez;

    public JugadorSuplente(String nombre, int edad) {
        super(nombre, edad);
        this.partidosIngresadosDesdeBanco = 0;
        this.haIngresadoAlgunaVez = false;
    }

    public int getPartidosIngresadosDesdeBanco() {
        return partidosIngresadosDesdeBanco;
    }

    public void setPartidosIngresadosDesdeBanco(int partidosIngresadosDesdeBanco) {
        this.partidosIngresadosDesdeBanco = partidosIngresadosDesdeBanco;
        if (partidosIngresadosDesdeBanco > 0) {
            this.haIngresadoAlgunaVez = true;
        }
    }

    public boolean haIngresadoAlgunaVez() {
        return haIngresadoAlgunaVez;
    }


    @Override
    public void incrementarGoles(int goles) {
        this.cantidadGoles += goles;
    }


    @Override
    public String obtenerTipoJugador() {
        return "Suplente";
    }

    @Override
    public String toString() {
        return super.toString() + ", Partidos Ingresados desde el Banco: " + partidosIngresadosDesdeBanco + " (Suplente)";
    }
}