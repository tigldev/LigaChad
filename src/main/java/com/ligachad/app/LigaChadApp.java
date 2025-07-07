package com.ligachad.app;

import com.ligachad.model.Equipo;
import com.ligachad.model.Jugador;
import com.ligachad.model.Partido;
import com.ligachad.service.LigaService;
import com.ligachad.service.LigaServiceImpl;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class LigaChadApp {

    private LigaService ligaService;
    private Scanner scanner;

    public LigaChadApp() {
        this.ligaService = new LigaServiceImpl();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        LigaChadApp app = new LigaChadApp();
        app.run();
    }
    
    public void run() {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    registrarJugador();
                    break;
                case 2:
                    crearEquipo();
                    break;
                case 3:
                    incorporarJugadorAEquipo();
                    break;
                case 4:
                    registrarPartido();
                    break;
                case 5:
                    asignarGolesAJugadorEnPartido();
                    break;
                case 6:
                    ligaService.mostrarListadoTodosJugadores();
                    break;
                case 7:
                    ligaService.mostrarGoleadorDeLaLiga();
                    break;
                case 8:
                    ligaService.mostrarPromedioGolesPorEquipo();
                    break;
                case 9:
                    ligaService.mostrarRankingEquiposPorGoles();
                    break;
                case 10:
                    transferirJugador();
                    break;
                case 11:
                    ligaService.mostrarSuplentesNuncaIngresados();
                    break;
                case 12:
                    ligaService.mostrarTitularConMasMinutosJugados();
                    break;
                case 13:
                    exportarJugadoresEquipoCSV();
                    break;
                case 14:
                    mostrarMenuReportes();
                    break;
                case 0:
                    System.out.println("¡Gracias por usar el sistema de la Liga de Fútbol Chad! ¡Hasta pronto!");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, intente de nuevo.");
            }
            System.out.println("\nPresione Enter para continuar...");
            scanner.nextLine();
            scanner.nextLine();
        } while (opcion != 0);
        scanner.close();
    }

    private void mostrarMenu() {
        System.out.println("\n--- Menú Liga de Fútbol Chad ---");
        System.out.println("1. Registrar Jugador (Titular/Suplente)");
        System.out.println("2. Crear Equipo");
        System.out.println("3. Incorporar Jugador a Equipo");
        System.out.println("4. Registrar Partido");
        System.out.println("5. Asignar Goles a Jugador en Partido");
        System.out.println("6. Mostrar Listado de Todos los Jugadores");
        System.out.println("7. Mostrar Goleador de la Liga");
        System.out.println("8. Mostrar Promedio de Goles por Partido de Cada Equipo");
        System.out.println("9. Mostrar Ranking de Equipos por Goles Anotados");
        System.out.println("10. Transferir Jugador de un Equipo a Otro");
        System.out.println("11. Mostrar Jugadores Suplentes que Nunca Han Ingresado");
        System.out.println("12. Mostrar Jugador Titular con Mayor Cantidad de Minutos Jugados");
        System.out.println("13. Exportar Jugadores de un Equipo a CSV");
        System.out.println("14. Reportes (General de Liga / Por Equipo)");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private int leerOpcion() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, ingrese un número.");
            scanner.nextLine();
            return -1;
        } finally {
            scanner.nextLine();
        }
    }

    private void registrarJugador() {
        System.out.print("Ingrese el nombre del jugador: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese la edad del jugador: ");
        int edad = leerEntero();
        System.out.print("¿Es titular o suplente? (titular/suplente): ");
        String tipo = scanner.nextLine();
        ligaService.registrarJugador(tipo, nombre, edad);
    }

    private void crearEquipo() {
        System.out.print("Ingrese el nombre del equipo: ");
        String nombreEquipo = scanner.nextLine();
        ligaService.crearEquipo(nombreEquipo);
    }

    private void incorporarJugadorAEquipo() {
        System.out.print("Ingrese el nombre del jugador a incorporar: ");
        String nombreJugador = scanner.nextLine();
        System.out.print("Ingrese el nombre del equipo al que se incorporará: ");
        String nombreEquipo = scanner.nextLine();
        ligaService.incorporarJugadorAEquipo(nombreJugador, nombreEquipo);
    }

    private void registrarPartido() {
        System.out.println("Equipos disponibles:");
        ligaService.getEquipos().forEach(e -> System.out.println("- " + e.getNombre()));
        System.out.print("Ingrese el nombre del equipo local: ");
        String local = scanner.nextLine();
        System.out.print("Ingrese el nombre del equipo visitante: ");
        String visitante = scanner.nextLine();
        Partido partido = ligaService.registrarPartido(local, visitante);
        if (partido != null) {
            System.out.println("Partido registrado: " + partido.getEquipoLocal().getNombre() + " vs " + partido.getEquipoVisitante().getNombre());
        }
    }

    private void asignarGolesAJugadorEnPartido() {
        List<Partido> partidosActivos = ligaService.getPartidos();
        if (partidosActivos.isEmpty()) {
            System.out.println("No hay partidos registrados para asignar goles.");
            return;
        }

        System.out.println("Partidos registrados (formato: EquipoA vs EquipoB):");
        partidosActivos.forEach(p -> System.out.println("- " + p.getEquipoLocal().getNombre() + " vs " + p.getEquipoVisitante().getNombre()));

        System.out.print("Ingrese el nombre del partido (ej. 'EquipoA vs EquipoB'): ");
        String nombrePartidoStr = scanner.nextLine();
        System.out.print("Ingrese el nombre del jugador que anotó: ");
        String nombreJugador = scanner.nextLine();
        System.out.print("Ingrese la cantidad de goles: ");
        int goles = leerEntero();

        if (goles > 0) {

            boolean golesAsignados = ligaService.asignarGolesAJugadorEnPartido(nombrePartidoStr, nombreJugador, goles);

            if (golesAsignados) {
                ligaService.buscarJugadorPorNombre(nombreJugador).ifPresent(j -> {
                    if (j instanceof com.ligachad.model.JugadorTitular) {
                        System.out.print("Ingrese los minutos jugados por " + j.getNombre() + ": ");
                        int minutos = leerEntero();
                        ((com.ligachad.model.JugadorTitular) j).setMinutosJugados(((com.ligachad.model.JugadorTitular) j).getMinutosJugados() + minutos);
                    } else if (j instanceof com.ligachad.model.JugadorSuplente) {
                        System.out.print("¿Ingresó " + j.getNombre() + " en este partido? (si/no): ");
                        String ingreso = scanner.nextLine();
                        if ("si".equalsIgnoreCase(ingreso)) {
                            ((com.ligachad.model.JugadorSuplente) j).setPartidosIngresadosDesdeBanco(((com.ligachad.model.JugadorSuplente) j).getPartidosIngresadosDesdeBanco() + 1);
                        }
                    }
                });
            }
        } else {
            System.out.println("La cantidad de goles debe ser positiva.");
        }
    }

    private void transferirJugador() {
        System.out.print("Ingrese el nombre del jugador a transferir: ");
        String nombreJugador = scanner.nextLine();
        System.out.print("Ingrese el nombre del equipo de origen: ");
        String nombreOrigen = scanner.nextLine();
        System.out.print("Ingrese el nombre del equipo de destino: ");
        String nombreDestino = scanner.nextLine();
        ligaService.transferirJugador(nombreJugador, nombreOrigen, nombreDestino);
    }

    private void exportarJugadoresEquipoCSV() {
        System.out.print("Ingrese el nombre del equipo cuyos jugadores desea exportar: ");
        String nombreEquipo = scanner.nextLine();
        System.out.print("Ingrese el nombre del archivo CSV (ej. jugadores.csv): ");
        String nombreArchivo = scanner.nextLine();
        ligaService.exportarJugadoresEquipoCSV(nombreEquipo, nombreArchivo);
    }

    private void mostrarMenuReportes() {
        int opcionReporte;
        do {
            System.out.println("\n--- Menú de Reportes ---");
            System.out.println("1. Reporte General de Liga");
            System.out.println("2. Reporte por Equipo");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción de reporte: ");
            opcionReporte = leerOpcion();

            switch (opcionReporte) {
                case 1:
                    ligaService.generarReporteGeneralLiga();
                    break;
                case 2:
                    System.out.print("Ingrese el nombre del equipo para el reporte: ");
                    String nombreEquipoReporte = scanner.nextLine();
                    ligaService.generarReporteDeEquipo(nombreEquipoReporte);
                    break;
                case 0:
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, intente de nuevo.");
            }
            if (opcionReporte != 0) {
                System.out.println("\nPresione Enter para continuar...");
                scanner.nextLine();
            }
        } while (opcionReporte != 0);
    }

    // Método de utilidad para leer enteros con manejo de excepciones
    private int leerEntero() {
        while (true) {
            try {
                int valor = scanner.nextInt();
                scanner.nextLine();
                return valor;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número entero.");
                scanner.nextLine();
                System.out.print("Intente de nuevo: ");
            }
        }
    }
}
