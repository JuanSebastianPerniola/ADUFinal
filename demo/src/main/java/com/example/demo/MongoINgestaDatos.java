package com.example.demo;

import java.io.File;
import java.nio.file.*;
import java.time.LocalDate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class MongoDbBatchIngestionModule {

    private static final Logger logger = LoggerFactory.getLogger(MongoDbBatchIngestionModule.class);

    @Value("${ingestion.batch.directory.pending}") // Mismo directorio que BatchIngestionModule
    private String pendingDirectory;

    @Value("${ingestion.batch.directory.correct}")
    private String correctDirectory;

    @Value("${ingestion.batch.directory.error}")
    private String errorDirectory;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    // Procesa cada 5 minutos (ajustable)
    @Scheduled(fixedRateString = "${ingestion.mongodb.schedule.rate:300000}")
    public void processFiles() {
        logger.info("Starting MongoDB batch ingestion");
        processJsonFilesForMongo();
        logger.info("MongoDB batch ingestion completed");
    }

    private void processJsonFilesForMongo() {
        File[] jsonFiles = new File(pendingDirectory).listFiles(
                (dir, name) -> name.toLowerCase().endsWith(".json")
        );

        if (jsonFiles != null) {
            for (File jsonFile : jsonFiles) {
                try {
                    logger.info("Processing JSON for MongoDB: {}", jsonFile.getName());
                    MongoReserva mongoReserva = parseJsonForMongo(jsonFile);
                    mongoTemplate.save(mongoReserva, "reservas");
                    moveFile(jsonFile, correctDirectory);
                } catch (Exception e) {
                    logger.error("Error processing JSON for MongoDB: {}", jsonFile.getName(), e);
                    moveFile(jsonFile, errorDirectory);
                }
            }
        }
    }

    private MongoReserva parseJsonForMongo(File jsonFile) throws Exception {
        JsonNode root = objectMapper.readTree(jsonFile);
        MongoReserva mongoReserva = new MongoReserva();

        // Mapeo de campos (personaliza según tu modelo MongoReserva)
        mongoReserva.setExternalId(root.path("id").asText());
        mongoReserva.setCheckIn(root.path("checkIn").asText());
        mongoReserva.setCheckOut(root.path("checkOut").asText());

        // Ejemplo para cliente (ajusta campos)
        if (root.has("persona")) {
            JsonNode persona = root.get("persona");
            mongoReserva.setClienteNombre(persona.path("nombre").asText());
            mongoReserva.setClienteEmail(persona.path("email").asText());
        }

        // Guarda el JSON original como String (opcional)
        mongoReserva.setOriginalJson(root.toString());
        mongoReserva.setIngestedAt(LocalDate.now().toString());

        return mongoReserva;
    }

    private void moveFile(File file, String targetDir) {
        try {
            Path targetPath = Paths.get(targetDir, file.getName());
            Files.createDirectories(targetPath.getParent());
            Files.move(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("Failed to move file: {}", file.getName(), e);
        }
    }
}