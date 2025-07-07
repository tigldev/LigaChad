package com.ligachad.service;

import com.ligachad.model.Equipo;
import com.ligachad.model.Jugador;
import com.ligachad.model.JugadorSuplente;
import com.ligachad.model.JugadorTitular;
import com.ligachad.model.Partido;
import com.ligachad.service.impl.EquipoServiceImpl;
import com.ligachad.service.impl.EstadisticasServiceImpl;
import com.ligachad.service.impl.JugadorServiceImpl;
import com.ligachad.service.impl.PartidoServiceImpl;
import com.ligachad.util.CsvExporter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class LigaServiceImpl implements LigaService {

    private JugadorService jugadorService;
    private EquipoService equipoService;
    private PartidoService partidoService;
    private EstadisticasService estadisticasService;

    public LigaServiceImpl() {
        this.jugadorService = new JugadorServiceImpl();
        this.equipoService = new EquipoServiceImpl();
        this.partidoService = new PartidoServiceImpl();
        this.estadisticasService = new EstadisticasServiceImpl();
    }


    @Override
    public Jugador registrarJugador(String tipoJugador, String nombre, int edad) {
        return jugadorService.registrarJugador(tipoJugador, nombre, edad);
    }

    @Override
    public Optional<Jugador> buscarJugadorPorNombre(String nombre) {
        return jugadorService.buscarJugadorPorNombre(nombre);
    }

    @Override
    public void mostrarListadoTodosJugadores() {
        if (jugadorService.getAllJugadores().isEmpty()) {
            System.out.println("No hay jugadores registrados en la liga.");
            return;
        }
        System.out.println("\n--- Listado de Todos los Jugadores ---");
        jugadorService.getAllJugadores().forEach(System.out::println);
        System.out.println("-------------------------------------");
    }


    @Override
    public Equipo crearEquipo(String nombreEquipo) {
        return equipoService.crearEquipo(nombreEquipo);
    }

    @Override
    public Optional<Equipo> buscarEquipoPorNombre(String nombreEquipo) {
        return equipoService.buscarEquipoPorNombre(nombreEquipo);
    }


    @Override
    public Partido registrarPartido(String nombreEquipoLocal, String nombreEquipoVisitante) {
        Optional<Equipo> localOpt = equipoService.buscarEquipoPorNombre(nombreEquipoLocal);
        Optional<Equipo> visitanteOpt = equipoService.buscarEquipoPorNombre(nombreEquipoVisitante);

        if (localOpt.isEmpty()) {
            System.out.println("Error: Equipo local '" + nombreEquipoLocal + "' no encontrado.");
            return null;
        }
        if (visitanteOpt.isEmpty()) {
            System.out.println("Error: Equipo visitante '" + nombreEquipoVisitante + "' no encontrado.");
            return null;
        }

        return partidoService.registrarPartido(localOpt.get(), visitanteOpt.get());
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

        Optional<Partido> partidoOpt = partidoService.buscarPartidoPorNombre(nombreEquipo1, nombreEquipo2);

        if (partidoOpt.isEmpty()) {
            System.out.println("Error: Partido '" + nombrePartidoStr + "' no encontrado.");
            return false;
        }

        Optional<Jugador> jugadorOpt = jugadorService.buscarJugadorPorNombre(nombreJugador);
        if (jugadorOpt.isEmpty()) {
            System.out.println("Error: Jugador '" + nombreJugador + "' no encontrado.");
            return false;
        }

        return partidoService.asignarGolesAJugadorEnPartido(partidoOpt.get(), jugadorOpt.get(), goles);
    }


    @Override
    public void mostrarGoleadorDeLaLiga() {
        Optional<Jugador> goleador = estadisticasService.getGoleadorDeLaLiga(jugadorService.getAllJugadores());
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
        if (equipoService.getAllEquipos().isEmpty()) {
            System.out.println("No hay equipos registrados.");
            return;
        }
        System.out.println("\n--- Promedio de Goles por Equipo (por partido) ---");
        estadisticasService.getPromedioGolesPorEquipo(equipoService.getAllEquipos(), partidoService.getAllPartidos())
                .forEach((equipo, promedio) -> {
                    long partidosJugados = partidoService.getAllPartidos().stream()
                            .filter(p -> p.getEquipoLocal().equals(equipo) || p.getEquipoVisitante().equals(equipo))
                            .count();
                    int golesTotalesEquipo = 0;
                    for (Partido p : partidoService.getAllPartidos()) {
                        if (p.getEquipoLocal().equals(equipo)) {
                            golesTotalesEquipo += p.getResultadoLocal();
                        } else if (p.getEquipoVisitante().equals(equipo)) {
                            golesTotalesEquipo += p.getResultadoVisitante();
                        }
                    }
                    if (partidosJugados > 0) {
                        System.out.printf("Equipo %s: %.2f goles por partido (Total Goles: %d, Partidos Jugados: %d)%n", equipo.getNombre(), promedio, golesTotalesEquipo, partidosJugados);
                    } else {
                        System.out.println("Equipo " + equipo.getNombre() + ": No ha jugado partidos.");
                    }
                });
        System.out.println("-------------------------------------------------");
    }

    @Override
    public void mostrarRankingEquiposPorGoles() {
        if (equipoService.getAllEquipos().isEmpty()) {
            System.out.println("No hay equipos registrados.");
            return;
        }
        System.out.println("\n--- Ranking de Equipos por Cantidad de Goles Anotados ---");
        estadisticasService.getRankingEquiposPorGoles(equipoService.getAllEquipos(), partidoService.getAllPartidos()).entrySet().stream()
                .sorted(java.util.Map.Entry.comparingByValue(java.util.Comparator.reverseOrder()))
                .forEach(entry -> System.out.println("Equipo: " + entry.getKey().getNombre() + " - Goles Anotados: " + entry.getValue()));
        System.out.println("---------------------------------------------------------");
    }

    @Override
    public void mostrarSuplentesNuncaIngresados() {
        List<JugadorSuplente> suplentesNuncaIngresados = estadisticasService.getSuplentesNuncaIngresados(jugadorService.getAllJugadores());
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
        Optional<JugadorTitular> titularConMasMinutos = estadisticasService.getTitularConMasMinutosJugados(jugadorService.getAllJugadores());
        if (titularConMasMinutos.isPresent()) {
            System.out.println("\n--- Jugador Titular con Mayor Cantidad de Minutos Jugados ---");
            System.out.println(titularConMasMinutos.get());
            System.out.println("-----------------------------------------------------------");
        } else {
            System.out.println("No hay jugadores titulares registrados o con minutos jugados.");
        }
    }

    @Override
    public void generarReporteGeneralLiga() {
        estadisticasService.generarReporteGeneralLiga(jugadorService.getAllJugadores(), equipoService.getAllEquipos(), partidoService.getAllPartidos());
    }

    @Override
    public void generarReporteDeEquipo(String nombreEquipo) {
        Optional<Equipo> equipoOpt = equipoService.buscarEquipoPorNombre(nombreEquipo);
        if (equipoOpt.isEmpty()) {
            System.out.println("Error: Equipo '" + nombreEquipo + "' no encontrado para generar reporte.");
            return;
        }
        estadisticasService.generarReporteDeEquipo(equipoOpt.get(), partidoService.getAllPartidos());
    }


    @Override
    public boolean incorporarJugadorAEquipo(String nombreJugador, String nombreEquipo) {
        Optional<Jugador> jugadorOpt = jugadorService.buscarJugadorPorNombre(nombreJugador);
        Optional<Equipo> equipoOpt = equipoService.buscarEquipoPorNombre(nombreEquipo);

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

        if (equipoService.addJugadorToEquipo(jugador, equipo)) {
            System.out.println("Jugador '" + nombreJugador + "' incorporado al equipo '" + nombreEquipo + "'.");
            return true;
        }
        return false;
    }

    @Override
    public boolean transferirJugador(String nombreJugador, String nombreEquipoOrigen, String nombreEquipoDestino) {
        Optional<Jugador> jugadorOpt = jugadorService.buscarJugadorPorNombre(nombreJugador);
        Optional<Equipo> equipoOrigenOpt = equipoService.buscarEquipoPorNombre(nombreEquipoOrigen);
        Optional<Equipo> equipoDestinoOpt = equipoService.buscarEquipoPorNombre(nombreEquipoDestino);

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

        equipoService.removeJugadorFromEquipo(jugador, equipoOrigen);
        equipoService.addJugadorToEquipo(jugador, equipoDestino);
        System.out.println("Jugador '" + nombreJugador + "' transferido de '" + nombreEquipoOrigen + "' a '" + nombreEquipoDestino + "'.");
        return true;
    }

    @Override
    public boolean exportarJugadoresEquipoCSV(String nombreEquipo, String nombreArchivo) {
        Optional<Equipo> equipoOpt = equipoService.buscarEquipoPorNombre(nombreEquipo);
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
            CsvExporter.exportarJugadoresACsv(nombreArchivo, jugadoresDelEquipo);
            System.out.println("Jugadores del equipo '" + nombreEquipo + "' exportados a '" + nombreArchivo + "' exitosamente.");
            return true;
        } catch (IOException e) {
            System.err.println("Error al exportar jugadores a CSV: " + e.getMessage());
            return false;
        }
    }


    @Override
    public List<Jugador> getJugadores() {
        return jugadorService.getAllJugadores();
    }

    @Override
    public List<Equipo> getEquipos() {
        return equipoService.getAllEquipos();
    }

    @Override
    public List<Partido> getPartidos() {
        return partidoService.getAllPartidos();
    }
}
