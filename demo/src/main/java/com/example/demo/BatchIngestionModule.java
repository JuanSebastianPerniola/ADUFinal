package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.example.demo.HabitacionesServiceJPA.IHabitacionJPA;
import com.example.demo.HotelServiceJpa.IHotelJPA;
import com.example.demo.PersonaServicio.PersonaRepository;
import com.example.demo.model.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.model.Reserva;
import com.example.demo.model.Habitaciones;
import com.example.demo.Reserva.*;
import com.example.demo.model.Persona;
import com.example.demo.PersonaServicio.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private IReservaJPA reservationRepository;
    @Autowired
    private IHotelJPA iHotelJPA;
    @Autowired
    private IHabitacionJPA iHabitacionJPA;
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PersonaReservationProcessor personaProcessor;

    @Scheduled(fixedRateString = "${ingestion.batch.schedule.rate:600000}")
    public void processFiles() {
        logger.info("Starting batch ingestion process");
        processXmlFiles();
        processJsonFiles();
        logger.info("Batch ingestion process completed");
    }

    private void processXmlFiles() {
        File directory = new File(pendingDirectory);
        File[] xmlFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));

        if (xmlFiles != null) {
            for (File xmlFile : xmlFiles) {
                try {
                    logger.info("Processing XML file: {}", xmlFile.getName());
                    Reserva reserva = parseXmlReservation(xmlFile);

                    // Create a new persona for this reservation
                    Persona originalPersona = reserva.getPersona();
                    reserva = personaProcessor.createNewPersonaForReservation(originalPersona, reserva);

                    reservationRepository.save(reserva);
                    moveFile(xmlFile, correctDirectory);
                    logger.info("Successfully processed XML file: {}", xmlFile.getName());
                } catch (Exception e) {
                    logger.error("Error processing XML file: {} - {}", xmlFile.getName(), e.getMessage(), e);
                    moveFile(xmlFile, errorDirectory);
                }
            }
        }
    }

    private void processJsonFiles() {
        File directory = new File(pendingDirectory);
        File[] jsonFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));

        if (jsonFiles != null) {
            for (File jsonFile : jsonFiles) {
                try {
                    logger.info("Processing JSON file: {}", jsonFile.getName());
                    Reserva reservation = parseJsonReservation(jsonFile);

                    // Create a new persona for this reservation
                    Persona originalPersona = reservation.getPersona();
                    reservation = personaProcessor.createNewPersonaForReservation(originalPersona, reservation);

                    reservationRepository.save(reservation);
                    moveFile(jsonFile, correctDirectory);
                    logger.info("Successfully processed JSON file: {}", jsonFile.getName());
                } catch (Exception e) {
                    logger.error("Error processing JSON file: {} - {}", jsonFile.getName(), e.getMessage(), e);
                    moveFile(jsonFile, errorDirectory);
                }
            }
        }
    }

    // Rest of your class remains the same...
    private Reserva parseXmlReservation(File xmlFile) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);
        document.getDocumentElement().normalize();

        Element rootElement = document.getDocumentElement();

        // Extract essential IDs from XML
        Long personaId = Long.parseLong(getElementTextContent(rootElement, "personaId"));
        Long hotelId = Long.parseLong(getElementTextContent(rootElement, "hotelId"));
        Long habitacionId = Long.parseLong(getElementTextContent(rootElement, "habitacionId"));
        String checkInStr = getElementTextContent(rootElement, "checkIn");
        String checkOutStr = getElementTextContent(rootElement, "checkOut");

        // Validate required fields
        if (personaId == null || hotelId == null || checkInStr == null || checkOutStr == null) {
            throw new IllegalArgumentException("XML debe contener personaId, hotelId y fechas");
        }

        // Find related entities
        Persona persona = personaRepository.findById(personaId)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con ID: " + personaId));

        Hotel hotel = iHotelJPA.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel no encontrado con ID: " + hotelId));

        Habitaciones habitacion = null;
        if (habitacionId != null) {
            habitacion = iHabitacionJPA.findById(habitacionId)
                    .orElseThrow(() -> new RuntimeException("Habitación no encontrada con ID: " + habitacionId));
        }

        // Create and return the reservation
        Reserva reserva = new Reserva();
        reserva.setPersona(persona);
        reserva.setHotel(hotel);
        reserva.setHabitacion(habitacion);
        reserva.setCheckIn(LocalDate.parse(checkInStr));
        reserva.setCheckOut(LocalDate.parse(checkOutStr));

        return reserva;
    }

    private String getElementTextContent(Element parent, String elementName) {
        NodeList nodeList = parent.getElementsByTagName(elementName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return null;
    }

    private Reserva parseJsonReservation(File jsonFile) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(jsonFile); // Lee el JSON

        // 1. Validar campos obligatorios
        if (!root.has("personaId") || !root.has("checkIn") || !root.has("hotelId")) {
            throw new IllegalArgumentException("JSON debe contener personaId, hotelId y fechas");
        }

        // 2. Buscar las entidades relacionadas
        Persona persona = personaRepository.findById(root.get("personaId").asLong())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con ID: " + root.get("personaId").asLong()));

        Hotel hotel = iHotelJPA.findById(root.get("hotelId").asLong())
                .orElseThrow(() -> new RuntimeException("Hotel no encontrado con ID: " + root.get("hotelId").asLong()));

        Habitaciones habitacion = iHabitacionJPA.findById(root.get("habitacionId").asLong())
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada con ID: " + root.get("habitacionId").asLong()));

        // 3. Crear y devolver la reserva
        Reserva reserva = new Reserva();
        reserva.setPersona(persona);
        reserva.setHotel(hotel);
        reserva.setHabitacion(habitacion);
        reserva.setCheckIn(LocalDate.parse(root.get("checkIn").asText()));
        reserva.setCheckOut(LocalDate.parse(root.get("checkOut").asText()));

        return reserva;
    }

    private void moveFile(File file, String targetDirectory) {
        try {
            Path sourcePath = file.toPath();
            Path targetPath = Paths.get(targetDirectory, file.getName());

            // Ensure target directory exists
            Files.createDirectories(Paths.get(targetDirectory));

            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Moved file {} to {}", file.getName(), targetDirectory);
        } catch (IOException e) {
            logger.error("Failed to move file {}: {}", file.getName(), e.getMessage(), e);
        }
    }
}