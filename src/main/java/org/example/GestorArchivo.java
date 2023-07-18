package org.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Getter
@Setter
@NoArgsConstructor
public class GestorArchivo {
    private String rutaDelArchivo;

    //--|Obtencion de rutas|--//
    public String obtenerRuta(String rutaRelativa) {
        Console console = System.console();
        if (console != null) {
            return generarRutaConsola(rutaRelativa);
        } else {
            return generarRutaIDE(rutaRelativa);
        }
    }

    private String generarRutaConsola(String rutaRelativa) {
        try {
            String userDir = obtenerRutaDeSistema("");
            File file = new File(userDir, rutaRelativa);
            return file.getAbsolutePath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.getLogger("Se ha provocado un error en la función 'generarRutaConsola'");
        }
    }

    private String generarRutaIDE(String rutaRelativa) {
        try {
            String userDir = obtenerRutaDeSistema("\\out\\artifacts\\GestorArchivo_jar\\");
            File file = new File(userDir, rutaRelativa);
            return file.getAbsolutePath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.getLogger("Se ha provocado un error en la función 'generarRutaIDE'");
        }
    }

    private String obtenerRutaDeSistema(String extencionDeRuta) {
        return System.getProperty("user.dir") + extencionDeRuta;
    }


    //--|Crear Carpetas|--//
    public void crearCarpeta(String rutaDirectorio) {
        Path directorioPath = Paths.get(rutaDirectorio);
        try {
            Files.createDirectory(directorioPath);
            System.out.println("Directorio creado exitosamente.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.getLogger("Se ha provocado un error en la función 'rutaDirectorio'");
        }
    }


    //--|leer Archivo|--//
    public void mostrarArchivoSimple() {
        try (CSVReader reader = new CSVReader(new FileReader(rutaDelArchivo))) {
            List<String[]> records = reader.readAll();

            mostrarCSVRecorrido(records);

        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);

        } finally {
            System.getLogger("Se ha provocado un error en la función 'leeArchivoSimple'");
        }
    }

    public List<String[]> retornarArchivoCSV(String rutaDelArchivo) {
        try {
            CSVReader reader = new CSVReader(new FileReader(rutaDelArchivo));
            return reader.readAll();
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        } finally {
            System.getLogger("Se ha provocado un error en la función 'generarRutaConsola'");
        }

    }

    public void mostrarCSVRecorrido(List<String[]> records) {
        for (String[] record : records) {
            for (String value : record) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }


    //--|filtrar Archivo|--//
    public void filtrarArchivo(String[] indice) {
        try {
            CSVReader lector = new CSVReader(new FileReader(rutaDelArchivo));
            String[] documentoCSV = lector.readNext();

            List<Integer> listaIndice = obtenerIndiceDeseados(documentoCSV, indice);
            devolverMatrizDeLaLectura(lector, listaIndice);

        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);

        } finally {
            System.getLogger("Se ha provocado un error en la función 'filtrarArchivo'");
        }

    }

    private List<Integer> obtenerIndiceDeseados(String[] documentoCSV, String[] indice) {
        int[] indices = filtrarIndices(indice, documentoCSV);

        return listarIndice(indices);
    }

    private int[] filtrarIndices(String[] indice, String[] documentoCSV) {
        int[] indices = new int[indice.length];
        Arrays.fill(indices, -1);

        //Aquí fue chatGPT, la veldad
        IntStream.range(0, documentoCSV.length)
                .forEach(i -> IntStream.range(0, indice.length)
                        .filter(j -> documentoCSV[i].equalsIgnoreCase(indice[j]))
                        .findFirst()
                        .ifPresent(j -> indices[j] = i));

        return indices;
    }

    private List<Integer> listarIndice(int[] indices) {
        List<Integer> indiceLista = new ArrayList<>();
        for (int index : indices) {
            indiceLista.add(index);
        }

        return indiceLista;
    }

    private void devolverMatrizDeLaLectura(CSVReader lector, List<Integer> listaIndice) {
        String[] registroDeLectura;
        List<String> lecturaDeRetorno = new ArrayList<>();

        try {
            while ((registroDeLectura = lector.readNext()) != null) {
                obtenerElContenidoDelIndice(lecturaDeRetorno, registroDeLectura, listaIndice);
            }

            //return lecturaDeRetorno;

        } catch (CsvValidationException | IOException e) {
            throw new RuntimeException(e);

        } finally {
            System.getLogger("Se ha provocado un error en la función 'rutaDirectorio'");
        }
    }

    private void obtenerElContenidoDelIndice(List<String> lecturaDeRetorno, String[] registroDeLectura, List<Integer> listaIndice) {
        String date = registroDeLectura[listaIndice.get(0)];
        String country = registroDeLectura[listaIndice.get(1)];
        String confirmed = registroDeLectura[listaIndice.get(2)];

        lecturaDeRetorno.add(date + " / " + country + " / " + confirmed);

    }


    //--|Escribir Archivo|--//
    public void crearArchivoCSV(String rutaDelArchivo, String[] data) {
        if (!validarLaExistenciaDeArchivos(rutaDelArchivo)) {
            escribirArchivoCSV(rutaDelArchivo, data);
        } else {
            System.out.println("El archivo ya existe. Intente con otro nombre o ruta");
        }

    }

    private void escribirArchivoCSV(String rutaDeGuardado, String[] data) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(rutaDeGuardado, true));
            writer.writeNext(data);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);

        } finally {
            System.getLogger("Se ha provocado un error en la función 'escribirArchivoCSV'");
        }
    }


    //--|Actualizar Archivo|--//
    public void updateDataInCSV(String rutaDelArchivo, String[] nuevoArchivo) {
        try {
            escribirArchivoCSV(rutaDelArchivo, nuevoArchivo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.getLogger("Se ha provocado un error en la función 'generarRutaConsola'");
        }
    }


    //--|eliminar Archivo|--//
    public void eliminarArchivo(String rutaDelArchivo) {
        if (borrarArchivoCSV(rutaDelArchivo)) {
            System.out.println("Se ha borrado el archivo");
        } else {
            System.out.println("El archivo NO existe. Intente con otro nombre o ruta");
        }
    }

    private boolean borrarArchivoCSV(String rutaDelArchivo){
        try {
            File file = new File(rutaDelArchivo);
            return file.delete();
        } catch (Exception e) {
            throw new RuntimeException(e);

        } finally {
            System.getLogger("Se ha provocado un error en la función 'rutaDelArchivo'");
        }

    }


    //--|Validaciones|--//
    public boolean validarLaExistenciaDeArchivos(String rutaAValidar) {
        File file = new File(rutaAValidar);
        return file.exists();
    }
}
