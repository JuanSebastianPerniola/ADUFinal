package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.io.*;
import java.nio.file.*;

@SpringBootApplication(scanBasePackages = "com.example.demo")
public class DemoApplication {
    private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);
    private static final String BATCH_SCRIPT = "C:/Users/JuanPrograma/OneDrive/Desktop/ADUFinal/ADUFinal/demo/src/main/java/com/example/demo/IngestaDeDatos/ProcesarArchivosLocales.java";

    public static void main(String[] args) {
        try {
            // 1. Ejecutar script batch
            if (!ejecutarScriptBatch()) {
                System.exit(1);
            }

            // 2. Configurar e iniciar Spring Boot
            SpringApplication app = new SpringApplication(DemoApplication.class);

            // Manejar específicamente la SilentExitException
            try {
                Environment env = app.run(args).getEnvironment();
                logStartupInfo(env);
            } catch (Exception e) {
                if (!isSilentExitException(e)) {
                    logger.error("Error al iniciar la aplicación", e);
                    System.exit(1);
                }
                // SilentExitException puede ser ignorada
            }

        } catch (Exception e) {
            logger.error("Error fatal en la inicialización", e);
            System.exit(1);
        }
    }

    private static boolean ejecutarScriptBatch() {
        try {
            Path batchPath = Paths.get(BATCH_SCRIPT).toAbsolutePath();
            logger.info("Ejecutando script batch: {}", batchPath);

            Process process = new ProcessBuilder()
                    .command("cmd.exe", "/c", batchPath.toString())
                    .inheritIO() // Muestra la salida en la consola
                    .start();

            return process.waitFor() == 0;

        } catch (Exception e) {
            logger.error("Error al ejecutar script batch", e);
            return false;
        }
    }

    private static void logStartupInfo(Environment env) {
        logger.info("\n----------------------------------------------------------\n" +
                        "Aplicación iniciada correctamente\n" +
                        "Perfil activo: {}\n" +
                        "URL local: http://localhost:{}\n" +
                        "----------------------------------------------------------",
                env.getActiveProfiles().length == 0 ? "default" : String.join(",", env.getActiveProfiles()),
                env.getProperty("server.port"));
    }

    private static boolean isSilentExitException(Throwable e) {
        while (e != null) {
            if (e.getClass().getName().contains("SilentExitException")) {
                return true;
            }
            e = e.getCause();
        }
        return false;
    }
}