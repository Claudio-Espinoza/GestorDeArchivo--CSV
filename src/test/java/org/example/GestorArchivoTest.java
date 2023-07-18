package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GestorArchivoTest {
    private GestorArchivo gestorArchivo;

    @BeforeEach
    void setUp() {
        gestorArchivo = new GestorArchivo();
    }

    @Test
    void obtenerRutaConIde() {
        String rutaEsperada = "C:\\Desarrollo\\Java\\General\\GestorArchivo\\src/main/java/org/example/COVID19.csv";
    }

}