package com.ligachad.service;

import com.ligachad.model.Equipo;
import com.ligachad.model.Jugador;
import com.ligachad.model.JugadorSuplente;
import com.ligachad.model.JugadorTitular;
import com.ligachad.model.Partido;
import com.ligachad.util.CsvExporter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class LigaServiceImpl implements LigaService {

    private List<Jugador> jugadores;
    private List<Equipo> equipos;
    private List<Partido> partidos;

    public LigaServiceImpl() {
        this.jugadores = new ArrayList<>();
        this.equipos = new ArrayList<>();
        this.partidos = new ArrayList<>();
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
    public boolean incorporarJugadorAEquipo(String nombreJugador, String nombreEquipo) {
        Optional<Jugador> jugadorOpt = buscarJugadorPorNombre(nombreJugador);
        Optional<Equipo> equipoOpt = buscarEquipoPorNombre(nombreEquipo);

        if (jugadorOpt.isEmpty()) {
            System.out.println("Error: Jugador '" + nombreJugador + "' no encontrado.");
            return false;
        }
        if (equipoOpt.isEmpty()) {
            System.out.println("Error: Equipo '" + nombreEquipo + "' no encontrado.");
            return false;
        }

        Jugador jugador = jugadorOpt.get();
        Equipo equipo = equipoOpt.get();

        if (jugador.getEquipo() != null) {
            System.out.println("Error: El jugador '" + nombreJugador + "' ya pertenece al equipo '" + jugador.getEquipo().getNombre() + "'.");
            return false;
        }

        equipo.agregarJugador(jugador);
        System.out.println("Jugador '" + nombreJugador + "' incorporado al equipo '" + nombreEquipo + "'.");
        return true;
    }

    @Override
    public Partido registrarPartido(String nombreEquipoLocal, String nombreEquipoVisitante) {
        Optional<Equipo> localOpt = buscarEquipoPorNombre(nombreEquipoLocal);
        Optional<Equipo> visitanteOpt = buscarEquipoPorNombre(nombreEquipoVisitante);

        if (localOpt.isEmpty()) {
            System.out.println("Error: Equipo local '" + nombreEquipoLocal + "' no encontrado.");
            return null;
        }
        if (visitanteOpt.isEmpty()) {
            System.out.println("Error: Equipo visitante '" + nombreEquipoVisitante + "' no encontrado.");
            return null;
        }
        if (localOpt.get().equals(visitanteOpt.get())) {
            System.out.println("Error: Los equipos local y visitante no pueden ser el mismo.");
            return null;
        }

        Partido nuevoPartido = new Partido(localOpt.get(), visitanteOpt.get());
        partidos.add(nuevoPartido);
        System.out.println("Partido entre '" + nombreEquipoLocal + "' y '" + nombreEquipoVisitante + "' registrado.");
        return nuevoPartido;
    }

    @Override
    public boolean asignarGolesAJugadorEnPartido(String nombrePartidoStr, String nombreJugador, int goles) {
        String[] partes = nombrePartidoStr.split(" vs ");
        if (partes.length != 2) {
            System.out.println("Error: Formato de nombre de partido inválido. Use 'EquipoA vs EquipoB'.");
            return false;
        }
        String nombreEquipo1 = partes[0].trim();
        String nombreEquipo2 = partes[1].trim();

        Optional<Partido> partidoOpt = partidos.stream()
                .filter(p -> (p.getEquipoLocal().getNombre().equalsIgnoreCase(nombreEquipo1) && p.getEquipoVisitante().getNombre().equalsIgnoreCase(nombreEquipo2)) ||
                        (p.getEquipoLocal().getNombre().equalsIgnoreCase(nombreEquipo2) && p.getEquipoVisitante().getNombre().equalsIgnoreCase(nombreEquipo1)))
                .findFirst();

        if (partidoOpt.isEmpty()) {
            System.out.println("Error: Partido '" + nombrePartidoStr + "' no encontrado.");
            return false;
        }

        Partido partido = partidoOpt.get();
        Optional<Jugador> jugadorOpt = buscarJugadorPorNombre(nombreJugador);

        if (jugadorOpt.isEmpty()) {
            System.out.println("Error: Jugador '" + nombreJugador + "' no encontrado.");
            return false;
        }

        Jugador jugador = jugadorOpt.get();


        if (jugador.getEquipo() == null || (!jugador.getEquipo().equals(partido.getEquipoLocal()) && !jugador.getEquipo().equals(partido.getEquipoVisitante()))) {
            System.out.println("Error: El jugador '" + nombreJugador + "' no pertenece a ninguno de los equipos de este partido.");
            return false;
        }

        partido.registrarGol(jugador, goles);
        System.out.println("Goles asignados: " + goles + " a " + nombreJugador + " en el partido " + nombrePartidoStr);
        return true;
    }

    @Override
    public void mostrarListadoTodosJugadores() {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores registrados en la liga.");
            return;
        }
        System.out.println("\n--- Listado de Todos los Jugadores ---");
        jugadores.forEach(System.out::println);
        System.out.println("-------------------------------------");
    }

    @Override
    public void mostrarGoleadorDeLaLiga() {
        Optional<Jugador> goleador = jugadores.stream()
                .max(Comparator.comparingInt(Jugador::getCantidadGoles));

        if (goleador.isPresent()) {
            System.out.println("\n--- Goleador de la Liga ---");
            System.out.println("El goleador de la liga es: " + goleador.get().getNombre() + " con " + goleador.get().getCantidadGoles() + " goles.");
            System.out.println("--------------------------");
        } else {
            System.out.println("No hay jugadores con goles registrados.");
        }
    }

    @Override
    public void mostrarPromedioGolesPorEquipo() {
        if (equipos.isEmpty()) {
            System.out.println("No hay equipos registrados.");
            return;
        }
        System.out.println("\n--- Promedio de Goles por Equipo (por partido) ---");
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
                double promedio = (double) golesTotalesEquipo / partidosJugados;
                System.out.printf("Equipo %s: %.2f goles por partido (Total Goles: %d, Partidos Jugados: %d)%n", equipo.getNombre(), promedio, golesTotalesEquipo, partidosJugados);
            } else {
                System.out.println("Equipo " + equipo.getNombre() + ": No ha jugado partidos.");
            }
        }
        System.out.println("-------------------------------------------------");
    }

    @Override
    public void mostrarRankingEquiposPorGoles() {
        if (equipos.isEmpty()) {
            System.out.println("No hay equipos registrados.");
            return;
        }
        System.out.println("\n--- Ranking de Equipos por Cantidad de Goles Anotados ---");
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

        golesPorEquipo.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> System.out.println("Equipo: " + entry.getKey().getNombre() + " - Goles Anotados: " + entry.getValue()));
        System.out.println("---------------------------------------------------------");
    }

    @Override
    public boolean transferirJugador(String nombreJugador, String nombreEquipoOrigen, String nombreEquipoDestino) {
        Optional<Jugador> jugadorOpt = buscarJugadorPorNombre(nombreJugador);
        Optional<Equipo> equipoOrigenOpt = buscarEquipoPorNombre(nombreEquipoOrigen);
        Optional<Equipo> equipoDestinoOpt = buscarEquipoPorNombre(nombreEquipoDestino);

        if (jugadorOpt.isEmpty()) {
            System.out.println("Error: Jugador '" + nombreJugador + "' no encontrado.");
            return false;
        }
        if (equipoOrigenOpt.isEmpty()) {
            System.out.println("Error: Equipo de origen '" + nombreEquipoOrigen + "' no encontrado.");
            return false;
        }
        if (equipoDestinoOpt.isEmpty()) {
            System.out.println("Error: Equipo de destino '" + nombreEquipoDestino + "' no encontrado.");
            return false;
        }

        Jugador jugador = jugadorOpt.get();
        Equipo equipoOrigen = equipoOrigenOpt.get();
        Equipo equipoDestino = equipoDestinoOpt.get();

        if (jugador.getEquipo() == null || !jugador.getEquipo().equals(equipoOrigen)) {
            System.out.println("Error: El jugador '" + nombreJugador + "' no pertenece al equipo de origen '" + nombreEquipoOrigen + "'.");
            return false;
        }
        if (equipoDestino.getJugadores().contains(jugador)) {
            System.out.println("Error: El jugador '" + nombreJugador + "' ya está en el equipo de destino '" + nombreEquipoDestino + "'.");
            return false;
        }

        equipoOrigen.quitarJugador(jugador);
        equipoDestino.agregarJugador(jugador);
        System.out.println("Jugador '" + nombreJugador + "' transferido de '" + nombreEquipoOrigen + "' a '" + nombreEquipoDestino + "'.");
        return true;
    }

    @Override
    public void mostrarSuplentesNuncaIngresados() {
        List<JugadorSuplente> suplentesNuncaIngresados = jugadores.stream()
                .filter(j -> j instanceof JugadorSuplente)
                .map(j -> (JugadorSuplente) j)
                .filter(js -> !js.haIngresadoAlgunaVez())
                .collect(Collectors.toList());

        if (suplentesNuncaIngresados.isEmpty()) {
            System.out.println("No hay jugadores suplentes que nunca hayan ingresado.");
        } else {
            System.out.println("\n--- Jugadores Suplentes que Nunca Han Ingresado ---");
            suplentesNuncaIngresados.forEach(System.out::println);
            System.out.println("--------------------------------------------------");
        }
    }

    @Override
    public void mostrarTitularConMasMinutosJugados() {
        Optional<JugadorTitular> titularConMasMinutos = jugadores.stream()
                .filter(j -> j instanceof JugadorTitular)
                .map(j -> (JugadorTitular) j)
                .max(Comparator.comparingInt(JugadorTitular::getMinutosJugados));

        if (titularConMasMinutos.isPresent()) {
            System.out.println("\n--- Jugador Titular con Mayor Cantidad de Minutos Jugados ---");
            System.out.println(titularConMasMinutos.get());
            System.out.println("-----------------------------------------------------------");
        } else {
            System.out.println("No hay jugadores titulares registrados o con minutos jugados.");
        }
    }

    @Override
    public boolean exportarJugadoresEquipoCSV(String nombreEquipo, String nombreArchivo) {
        Optional<Equipo> equipoOpt = buscarEquipoPorNombre(nombreEquipo);
        if (equipoOpt.isEmpty()) {
            System.out.println("Error: Equipo '" + nombreEquipo + "' no encontrado para exportar.");
            return false;
        }

        List<Jugador> jugadoresDelEquipo = equipoOpt.get().getJugadores();
        if (jugadoresDelEquipo.isEmpty()) {
            System.out.println("El equipo '" + nombreEquipo + "' no tiene jugadores para exportar.");
            return false;
        }

        try {
            // Nota: CsvExporter aún no ha sido creada, pero lo haremos en el próximo paso.
            // Por ahora, si tu IDE muestra un error de compilación, es normal.
            CsvExporter.exportarJugadoresACsv(nombreArchivo, jugadoresDelEquipo);
            System.out.println("Jugadores del equipo '" + nombreEquipo + "' exportados a '" + nombreArchivo + "' exitosamente.");
            return true;
        } catch (IOException e) {
            System.err.println("Error al exportar jugadores a CSV: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void generarReporteGeneralLiga() {
        System.out.println("\n--- Reporte General de Liga ---");

        int totalGolesLiga = jugadores.stream().mapToInt(Jugador::getCantidadGoles).sum();
        System.out.println("Total de goles anotados en la liga: " + totalGolesLiga);

        Optional<Jugador> jugadorMasEficiente = jugadores.stream()
                .max(Comparator.comparingInt(Jugador::getCantidadGoles));
        if (jugadorMasEficiente.isPresent()) {
            System.out.println("Jugador más eficiente (mayor goleador): " + jugadorMasEficiente.get().getNombre() + " (" + jugadorMasEficiente.get().getCantidadGoles() + " goles)");
        } else {
            System.out.println("No hay jugadores registrados o con goles para determinar el más eficiente.");
        }

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
    public void generarReporteDeEquipo(String nombreEquipo) {
        Optional<Equipo> equipoOpt = buscarEquipoPorNombre(nombreEquipo);
        if (equipoOpt.isEmpty()) {
            System.out.println("Error: Equipo '" + nombreEquipo + "' no encontrado para generar reporte.");
            return;
        }

        Equipo equipo = equipoOpt.get();
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

    @Override
    public List<Jugador> getJugadores() {
        return new ArrayList<>(jugadores);
    }

    @Override
    public List<Equipo> getEquipos() {
        return new ArrayList<>(equipos);
    }

    @Override
    public List<Partido> getPartidos() {
        return new ArrayList<>(partidos);
    }
}