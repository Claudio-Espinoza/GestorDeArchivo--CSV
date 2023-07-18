package org.example;


import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws CsvValidationException, IOException {
        GestorArchivo gestorArchivo = new GestorArchivo();
        gestorArchivo.setRutaDelArchivo(gestorArchivo.obtenerRuta("datasets\\COVID19.csv"));

        String ruta = gestorArchivo.obtenerRuta("resultados");
        String[] indice = {"Date", "Country", "Deaths"};
        gestorArchivo.crearArchivoCSV( ruta, indice);

    }
}