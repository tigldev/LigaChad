package com.ligachad.util;

import com.ligachad.model.Jugador;
import com.ligachad.model.JugadorSuplente;
import com.ligachad.model.JugadorTitular;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CsvExporter {

    private static final String CSV_DELIMITER = ",";
    private static final String CSV_NEW_LINE = "\n";
    private static final String CSV_HEADER = "Es titular SI/NO,Nombre,Edad,Cantidad de goles,Minutos Jugados/Partidos Ingresados";

    public static void exportarJugadoresACsv(String fileName, List<Jugador> jugadores) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.print(CSV_HEADER);
            writer.print(CSV_NEW_LINE);

            for (Jugador jugador : jugadores) {
                StringBuilder line = new StringBuilder();
                line.append(jugador instanceof JugadorTitular ? "SI" : "NO").append(CSV_DELIMITER);
                line.append(jugador.getNombre()).append(CSV_DELIMITER);
                line.append(jugador.getEdad()).append(CSV_DELIMITER);
                line.append(jugador.getCantidadGoles()).append(CSV_DELIMITER);

                if (jugador instanceof JugadorTitular) {
                    line.append(((JugadorTitular) jugador).getMinutosJugados());
                } else if (jugador instanceof JugadorSuplente) {
                    line.append(((JugadorSuplente) jugador).getPartidosIngresadosDesdeBanco());
                }
                writer.print(line.toString());
                writer.print(CSV_NEW_LINE);
            }
        }
    }
}
