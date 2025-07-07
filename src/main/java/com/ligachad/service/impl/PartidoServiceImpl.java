package com.ligachad.service.impl;

import com.ligachad.model.Equipo;
import com.ligachad.model.Jugador;
import com.ligachad.model.Partido;
import com.ligachad.service.PartidoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class PartidoServiceImpl implements PartidoService {

    private List<Partido> partidos;

    public PartidoServiceImpl() {
        this.partidos = new ArrayList<>();
    }

    @Override
    public Partido registrarPartido(Equipo equipoLocal, Equipo equipoVisitante) {
        if (equipoLocal == null || equipoVisitante == null) {
            System.out.println("Error: Equipos nulos.");
            return null;
        }
        if (equipoLocal.equals(equipoVisitante)) {
            System.out.println("Error: Los equipos local y visitante no pueden ser el mismo.");
            return null;
        }

        Partido nuevoPartido = new Partido(equipoLocal, equipoVisitante);
        partidos.add(nuevoPartido);
        System.out.println("Partido entre '" + equipoLocal.getNombre() + "' y '" + equipoVisitante.getNombre() + "' registrado.");
        return nuevoPartido;
    }

    @Override
    public boolean asignarGolesAJugadorEnPartido(Partido partido, Jugador goleador, int goles) {
        if (partido == null || goleador == null || goles <= 0) {
            System.out.println("Error: Datos de gol inválidos.");
            return false;
        }


        if (goleador.getEquipo() == null || (!goleador.getEquipo().equals(partido.getEquipoLocal()) && !goleador.getEquipo().equals(partido.getEquipoVisitante()))) {
            System.out.println("Error: El jugador '" + goleador.getNombre() + "' no pertenece a ninguno de los equipos de este partido.");
            return false;
        }

        partido.registrarGol(goleador, goles);
        System.out.println("Goles asignados: " + goles + " a " + goleador.getNombre() + " en el partido " + partido.getEquipoLocal().getNombre() + " vs " + partido.getEquipoVisitante().getNombre());
        return true;
    }

    @Override
    public List<Partido> getAllPartidos() {
        return new ArrayList<>(partidos);
    }

    @Override
    public Optional<Partido> buscarPartidoPorNombre(String nombreEquipo1, String nombreEquipo2) {
        return partidos.stream()
                .filter(p -> (p.getEquipoLocal().getNombre().equalsIgnoreCase(nombreEquipo1) && p.getEquipoVisitante().getNombre().equalsIgnoreCase(nombreEquipo2)) ||
                        (p.getEquipoLocal().getNombre().equalsIgnoreCase(nombreEquipo2) && p.getEquipoVisitante().getNombre().equalsIgnoreCase(nombreEquipo1)))
                .findFirst();
    }
}