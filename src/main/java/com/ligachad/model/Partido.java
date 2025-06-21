package com.ligachad.model;

import java.util.HashMap;
import java.util.Map;

public class Partido {
    private Equipo equipoLocal;
    private Equipo equipoVisitante;
    private int resultadoLocal;
    private int resultadoVisitante;
    private Map<String, Integer> golesPorJugador;

    public Partido(Equipo equipoLocal, Equipo equipoVisitante) {
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.resultadoLocal = 0;
        this.resultadoVisitante = 0;
        this.golesPorJugador = new HashMap<>();
    }


    public Equipo getEquipoLocal() {
        return equipoLocal;
    }

    public void setEquipoLocal(Equipo equipoLocal) {
        this.equipoLocal = equipoLocal;
    }

    public Equipo getEquipoVisitante() {
        return equipoVisitante;
    }

    public void setEquipoVisitante(Equipo equipoVisitante) {
        this.equipoVisitante = equipoVisitante;
    }

    public int getResultadoLocal() {
        return resultadoLocal;
    }

    public void setResultadoLocal(int resultadoLocal) {
        this.resultadoLocal = resultadoLocal;
    }

    public int getResultadoVisitante() {
        return resultadoVisitante;
    }

    public void setResultadoVisitante(int resultadoVisitante) {
        this.resultadoVisitante = resultadoVisitante;
    }

    public Map<String, Integer> getGolesPorJugador() {
        return golesPorJugador;
    }


    public void registrarGol(Jugador goleador, int goles) {
        if (goleador == null || goles <= 0) {
            System.out.println("Error: Goleador inválido o cantidad de goles no positiva.");
            return;
        }

        goleador.incrementarGoles(goles);
        golesPorJugador.merge(goleador.getNombre(), goles, Integer::sum);


        if (goleador.getEquipo().equals(equipoLocal)) {
            this.resultadoLocal += goles;
        } else if (goleador.getEquipo().equals(equipoVisitante)) {
            this.resultadoVisitante += goles;
        }
    }

    @Override
    public String toString() {
        return "Partido: " + equipoLocal.getNombre() + " " + resultadoLocal + " - " + resultadoVisitante + " " + equipoVisitante.getNombre();
    }
}