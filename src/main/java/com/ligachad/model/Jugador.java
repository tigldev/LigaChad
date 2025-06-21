package com.ligachad.model;

public abstract class Jugador {
    protected String nombre;
    protected int edad;
    protected int cantidadGoles;
    protected Equipo equipo;

    public Jugador(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
        this.cantidadGoles = 0;
        this.equipo = null;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getCantidadGoles() {
        return cantidadGoles;
    }


    public abstract void incrementarGoles(int goles);

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }


    public abstract String obtenerTipoJugador();

    @Override
    public String toString() {
        return "Nombre: " + nombre + ", Edad: " + edad + ", Goles: " + cantidadGoles;
    }
}