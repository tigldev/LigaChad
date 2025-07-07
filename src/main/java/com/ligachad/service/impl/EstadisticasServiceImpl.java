package com.ligachad.service.impl;

import com.ligachad.model.Equipo;
import com.ligachad.model.Jugador;
import com.ligachad.model.JugadorSuplente;
import com.ligachad.model.JugadorTitular;
import com.ligachad.model.Partido;
import com.ligachad.service.EstadisticasService;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


public class EstadisticasServiceImpl implements EstadisticasService {

    public EstadisticasServiceImpl() {
    }

    @Override
    public Optional<Jugador> getGoleadorDeLaLiga(List<Jugador> jugadores) {
        return jugadores.stream()
                .max(Comparator.comparingInt(Jugador::getCantidadGoles));
    }

    @Override
    public Map<Equipo, Double> getPromedioGolesPorEquipo(List<Equipo> equipos, List<Partido> partidos) {
        Map<Equipo, Double> promedioGoles = new HashMap<>();
        for (Equipo equipo : equipos) {
            long partidosJugados = partidos.stream()
                    .filter(p -> p.getEquipoLocal().equals(equipo) || p.getEquipoVisitante().equals(equipo))
                    .count();

            int golesTotalesEquipo = 0;
            for (Partido p : partidos) {
                if (p.getEquipoLocal().equals(equipo)) {
                    golesTotalesEquipo += p.getResultadoLocal();
                } else if (p.getEquipoVisitante().equals(equipo)) {
                    golesTotalesEquipo += p.getResultadoVisitante();
                }
            }

            if (partidosJugados > 0) {
                promedioGoles.put(equipo, (double) golesTotalesEquipo / partidosJugados);
            } else {
                promedioGoles.put(equipo, 0.0);
            }
        }
        return promedioGoles;
    }

    @Override
    public Map<Equipo, Integer> getRankingEquiposPorGoles(List<Equipo> equipos, List<Partido> partidos) {
        Map<Equipo, Integer> golesPorEquipo = new HashMap<>();
        for (Equipo equipo : equipos) {
            int totalGoles = 0;
            for (Partido p : partidos) {
                if (p.getEquipoLocal().equals(equipo)) {
                    totalGoles += p.getResultadoLocal();
                } else if (p.getEquipoVisitante().equals(equipo)) {
                    totalGoles += p.getResultadoVisitante();
                }
            }
            golesPorEquipo.put(equipo, totalGoles);
        }
        return golesPorEquipo;
    }

    @Override
    public List<JugadorSuplente> getSuplentesNuncaIngresados(List<Jugador> jugadores) {
        return jugadores.stream()
                .filter(j -> j instanceof JugadorSuplente)
                .map(j -> (JugadorSuplente) j)
                .filter(js -> !js.haIngresadoAlgunaVez())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<JugadorTitular> getTitularConMasMinutosJugados(List<Jugador> jugadores) {
        return jugadores.stream()
                .filter(j -> j instanceof JugadorTitular)
                .map(j -> (JugadorTitular) j)
                .max(Comparator.comparingInt(JugadorTitular::getMinutosJugados));
    }

    @Override
    public void generarReporteGeneralLiga(List<Jugador> jugadores, List<Equipo> equipos, List<Partido> partidos) {
        System.out.println("\n--- Reporte General de Liga ---");

        int totalGolesLiga = jugadores.stream().mapToInt(Jugador::getCantidadGoles).sum();
        System.out.println("Total de goles anotados en la liga: " + totalGolesLiga);

        Optional<Jugador> jugadorMasEficiente = getGoleadorDeLaLiga(jugadores);
        if (jugadorMasEficiente.isPresent()) {
            System.out.println("Jugador más eficiente (mayor goleador): " + jugadorMasEficiente.get().getNombre() + " (" + jugadorMasEficiente.get().getCantidadGoles() + " goles)");
        } else {
            System.out.println("No hay jugadores registrados o con goles para determinar el más eficiente.");
        }

        Map<Equipo, Integer> golesPorEquipo = getRankingEquiposPorGoles(equipos, partidos);
        Optional<Map.Entry<Equipo, Integer>> equipoMasGoleador = golesPorEquipo.entrySet().stream()
                .max(Map.Entry.comparingByValue());
        if (equipoMasGoleador.isPresent()) {
            System.out.println("Equipo con mayor cantidad de goles: " + equipoMasGoleador.get().getKey().getNombre() + " (" + equipoMasGoleador.get().getValue() + " goles)");
        } else {
            System.out.println("No hay equipos registrados o con goles para determinar el equipo más goleador.");
        }
        System.out.println("------------------------------");
    }

    @Override
    public void generarReporteDeEquipo(Equipo equipo, List<Partido> partidos) {
        if (equipo == null) {
            System.out.println("Error: Equipo nulo para generar reporte.");
            return;
        }

        System.out.println("\n--- Reporte del Equipo: " + equipo.getNombre() + " ---");

        if (equipo.getJugadores().isEmpty()) {
            System.out.println("El equipo no tiene jugadores registrados.");
            System.out.println("------------------------------------");
            return;
        }


        double promedioGolesJugadores = equipo.getJugadores().stream()
                .mapToInt(Jugador::getCantidadGoles)
                .average()
                .orElse(0.0);
        System.out.printf("Promedio de goles de sus jugadores: %.2f%n", promedioGolesJugadores);


        List<Jugador> jugadoresSinGoles = equipo.getJugadores().stream()
                .filter(j -> j.getCantidadGoles() == 0)
                .collect(Collectors.toList());
        if (!jugadoresSinGoles.isEmpty()) {
            System.out.println("Jugadores que no han anotado goles:");
            jugadoresSinGoles.forEach(j -> System.out.println("  - " + j.getNombre()));
        } else {
            System.out.println("Todos los jugadores del equipo han anotado goles.");
        }


        Optional<JugadorTitular> titularMasMinutos = equipo.getJugadores().stream()
                .filter(j -> j instanceof JugadorTitular)
                .map(j -> (JugadorTitular) j)
                .max(Comparator.comparingInt(JugadorTitular::getMinutosJugados));
        if (titularMasMinutos.isPresent()) {
            System.out.println("Titular con más minutos jugados: " + titularMasMinutos.get().getNombre() + " (" + titularMasMinutos.get().getMinutosJugados() + " minutos)");
        } else {
            System.out.println("No hay titulares en este equipo o no tienen minutos registrados.");
        }


        Optional<JugadorSuplente> suplenteMasUtilizado = equipo.getJugadores().stream()
                .filter(j -> j instanceof JugadorSuplente)
                .map(j -> (JugadorSuplente) j)
                .max(Comparator.comparingInt(JugadorSuplente::getPartidosIngresadosDesdeBanco));
        if (suplenteMasUtilizado.isPresent()) {
            System.out.println("Suplente más utilizado: " + suplenteMasUtilizado.get().getNombre() + " (" + suplenteMasUtilizado.get().getPartidosIngresadosDesdeBanco() + " partidos ingresados)");
        } else {
            System.out.println("No hay suplentes en este equipo o no han sido utilizados.");
        }
        System.out.println("------------------------------------");
    }
}