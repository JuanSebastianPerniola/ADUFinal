package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class BatchIngestionModule {

    private static final Logger logger = LoggerFactory.getLogger(BatchIngestionModule.class);

    @Value("${ingestion.batch.directory.pending}")
    private String pendingDirectory;

    @Value("${ingestion.batch.directory.correct}")
    private String correctDirectory;

    @Value("${ingestion.batch.directory.error}")
    private String errorDirectory;

    @Autowired
    private com.example.demo.UnifiedDataProcessingService dataProcessingService;

    @Scheduled(fixedRateString = "${ingestion.batch.schedule.rate:60000}")
    public void processFiles() {
        logger.info("Iniciando proceso de ingesta por lotes");

        try {
            ensureDirectoriesExist();
            processAllFiles();
        } catch (Exception e) {
            logger.error("Error en el proceso de ingesta por lotes: {}", e.getMessage(), e);
        }

        logger.info("Proceso de ingesta por lotes completado");
    }

    private void ensureDirectoriesExist() {
        try {
            Files.createDirectories(Paths.get(pendingDirectory));
            Files.createDirectories(Paths.get(correctDirectory));
            Files.createDirectories(Paths.get(errorDirectory));
        } catch (IOException e) {
            logger.error("Error al crear directorios: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudieron crear los directorios necesarios", e);
        }
    }

    private void processAllFiles() {
        File directory = new File(pendingDirectory);

        if (!directory.exists() || !directory.isDirectory()) {
            logger.error("El directorio de archivos pendientes no existe: {}", pendingDirectory);
            return;
        }

        File[] files = directory.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".xml") || name.toLowerCase().endsWith(".json"));

        if (files == null || files.length == 0) {
            logger.info("No se encontraron archivos para procesar en: {}", pendingDirectory);
            return;
        }

        logger.info("Encontrados {} archivos para procesar", files.length);

        for (File file : files) {
            try {
                logger.info("Procesando archivo: {}", file.getName());
                dataProcessingService.processReservationFromFile(file);
                moveFile(file, correctDirectory);
                logger.info("Archivo procesado exitosamente: {}", file.getName());
            } catch (Exception e) {
                logger.error("Error al procesar archivo {}: {}", file.getName(), e.getMessage(), e);
                moveFile(file, errorDirectory);
            }
        }
    }

    private void moveFile(File file, String targetDirectory) {
        try {
            Path sourcePath = file.toPath();
            Path targetPath = Paths.get(targetDirectory, file.getName());

            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Archivo movido exitosamente a: {}", targetPath);
        } catch (IOException e) {
            logger.error("Error al mover el archivo {}: {}", file.getName(), e.getMessage(), e);
        }
    }
}