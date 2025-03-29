package com.example.demo.IngestaDeDatos;

import com.example.demo.NewData.LectorDatos;
import com.example.demo.NewData.ProcesarArchivos;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ProcesarArchivosLocales {
    private static final Logger logger = LoggerFactory.getLogger(ProcesarArchivosLocales.class);
    private static final Path carpetaPendientes = Paths.get("reserves/pendents");
    private static final Path carpetaCorrectos = Paths.get("reserves/correctes");
    private static final Path carpetaErrores = Paths.get("reserves/errors");

    // Configuración ajustable
    private static final int INTERVALO_PROCESAMIENTO_SEG = 10;
    private static final int NUM_HILOS = 2;

    public static void main(String[] args) {
        inicializarDirectorios();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(NUM_HILOS);

        scheduler.scheduleAtFixedRate(
                ProcesarArchivosLocales::procesarArchivosLocales,
                0,
                INTERVALO_PROCESAMIENTO_SEG,
                TimeUnit.SECONDS
        );
    }

    private static void inicializarDirectorios() {
        try {
            Files.createDirectories(carpetaPendientes);
            Files.createDirectories(carpetaCorrectos);
            Files.createDirectories(carpetaErrores);
        } catch (IOException e) {
            logger.error("Error al crear directorios: {}", e.getMessage());
            System.exit(1);
        }
    }

    private static void procesarArchivosLocales() {
        try {
            File[] archivos = carpetaPendientes.toFile().listFiles();
            if (archivos == null || archivos.length == 0) {
                return;
            }

            Arrays.stream(archivos)
                    .parallel()
                    .forEach(ProcesarArchivosLocales::procesarArchivo);
        } catch (Exception e) {
            logger.error("Error general en el procesamiento: {}", e.getMessage());
        }
    }

    private static void procesarArchivo(File archivo) {
        Objects.requireNonNull(archivo, "El archivo no puede ser null");

        try {
            JsonNode jsonData = determinarTipoYProcesar(archivo);

            if (jsonData != null) {
                LectorDatos.procesarDatos(jsonData);
                moverArchivo(archivo, carpetaCorrectos);
                logger.info("Procesado exitosamente: {}", archivo.getName());
            }
        } catch (Exception e) {
            logger.error("Error procesando {}: {}", archivo.getName(), e.getMessage());
            moverArchivo(archivo, carpetaErrores);
        }
    }

    private static JsonNode determinarTipoYProcesar(File archivo) throws IOException {
        String nombreArchivo = archivo.getName().toLowerCase();

        if (nombreArchivo.endsWith(".xml")) {
            return ProcesarArchivos.convertXmlFileToJson(archivo);
        } else if (nombreArchivo.endsWith(".json")) {
            return ProcesarArchivos.readJsonFile(archivo);
        }

        logger.warn("Formato no soportado: {}", archivo.getName());
        return null;
    }

    private static void moverArchivo(File archivo, Path carpetaDestino) {
        Path origen = archivo.toPath();
        Path destino = carpetaDestino.resolve(archivo.getName());

        try {
            Files.move(origen, destino, StandardCopyOption.REPLACE_EXISTING);
            logger.debug("Archivo movido de {} a {}", origen, destino);
        } catch (IOException e) {
            logger.error("Error moviendo archivo {}: {}", archivo.getName(), e.getMessage());
        }
    }
}