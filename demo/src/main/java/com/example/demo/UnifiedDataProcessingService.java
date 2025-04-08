package com.example.demo;

import com.example.demo.model.Reserva;
import com.example.demo.model.Habitaciones;
import com.example.demo.model.Hotel;
import com.example.demo.model.Persona;
import com.example.demo.Reserva.IReservaJPA;
import com.example.demo.HabitacionesServiceJPA.IHabitacionJPA;
import com.example.demo.HotelServiceJpa.IHotelJPA;
import com.example.demo.PersonaServicio.PersonaRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class UnifiedDataProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(UnifiedDataProcessingService.class);

    @Autowired
    private IReservaJPA reservationRepository;

    @Autowired
    private IHotelJPA hotelRepository;

    @Autowired
    private IHabitacionJPA habitacionRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Process a reservation from a file based on its extension
     */
    @Transactional
    public Reserva processReservationFromFile(File file) throws Exception {
        String fileName = file.getName().toLowerCase();

        if (fileName.endsWith(".xml")) {
            return processXmlReservation(file);
        } else if (fileName.endsWith(".json")) {
            return processJsonReservation(file);
        } else {
            throw new UnsupportedOperationException("Formato de archivo no soportado: " + fileName);
        }
    }

    /**
     * Process XML reservation
     */
    private Reserva processXmlReservation(File xmlFile) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);
        document.getDocumentElement().normalize();

        Element rootElement = document.getDocumentElement();

        // Extraer datos como en el JSON
        Long personaId = Long.parseLong(getElementTextContent(rootElement, "personaId"));
        Long hotelId = Long.parseLong(getElementTextContent(rootElement, "hotelId"));
        Long habitacionId = Long.parseLong(getElementTextContent(rootElement, "habitacionId"));
        String checkInStr = getElementTextContent(rootElement, "checkIn");
        String checkOutStr = getElementTextContent(rootElement, "checkOut");

        // Validar fechas (si lo necesitas)
        // validateDates(checkInStr, checkOutStr);

        // Obtener entidades
        Persona persona = findPersonaById(personaId);
        Hotel hotel = findHotelById(hotelId);
        Habitaciones habitacion = findHabitacionById(habitacionId);

        // Crear reserva
        Reserva reserva = new Reserva();
        reserva.setPersona(persona);
        reserva.setHotel(hotel);
        reserva.setHabitacion(habitacion);
        reserva.setCheckIn(LocalDate.parse(checkInStr));
        reserva.setCheckOut(LocalDate.parse(checkOutStr));

        try {
            Reserva savedReserva = reservationRepository.save(reserva);
            logger.info("Reserva guardada exitosamente con ID: {}", savedReserva.getIdReserva());
            return savedReserva;
        } catch (Exception e) {
            logger.error("Error al guardar la reserva en la base de datos: {}", e.getMessage(), e);
            throw new RuntimeException("Error al guardar la reserva: " + e.getMessage(), e);
        }
    }
    /**
     * Process JSON reservation
     */
    private Reserva processJsonReservation(File jsonFile) throws IOException {
        JsonNode root = objectMapper.readTree(jsonFile);
        logger.info("Procesando JSON: {}", root.toString());

        // Validate required fields
        validateRequiredJsonFields(root);

        // Get IDs from JSON
        Long personaId = root.get("personaId").asLong();
        Long hotelId = root.get("hotelId").asLong();
        Long habitacionId = root.get("habitacionId").asLong();
        String checkInStr = root.get("checkIn").asText();
        String checkOutStr = root.get("checkOut").asText();

        // Validate dates
//        validateDates(checkInStr, checkOutStr);

        // Fetch entities from database or throw exceptions if not found
        Persona persona = findPersonaById(personaId);
        Hotel hotel = findHotelById(hotelId);
        Habitaciones habitacion = findHabitacionById(habitacionId);

        // Create the reservation
        Reserva reserva = new Reserva();
        reserva.setPersona(persona);
        reserva.setHotel(hotel);
        reserva.setHabitacion(habitacion);
        reserva.setCheckIn(LocalDate.parse(checkInStr));
        reserva.setCheckOut(LocalDate.parse(checkOutStr));

        try {
            Reserva savedReserva = reservationRepository.save(reserva);
            logger.info("Reserva guardada exitosamente con ID: {}", savedReserva.getIdReserva());
            return savedReserva;
        } catch (Exception e) {
            logger.error("Error al guardar la reserva en la base de datos: {}", e.getMessage(), e);
            throw new RuntimeException("Error al guardar la reserva: " + e.getMessage(), e);
        }
    }

    /**
     * Helper to get element text content
     */
    private String getElementTextContent(Element parent, String elementName) {
        NodeList nodeList = parent.getElementsByTagName(elementName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return null;
    }

    /**
     * Validate required JSON fields
     */
    private void validateRequiredJsonFields(JsonNode root) {
        if (!root.has("personaId") || !root.has("hotelId") || !root.has("habitacionId") ||
                !root.has("checkIn") || !root.has("checkOut")) {
            throw new IllegalArgumentException("Faltan campos obligatorios en el JSON");
        }
    }

    /**
     * Validate date formats and logic
     */
    private void validateDates(String checkInStr, String checkOutStr) {
        try {
            LocalDate checkIn = LocalDate.parse(checkInStr);
            LocalDate checkOut = LocalDate.parse(checkOutStr);

            if (checkOut.isBefore(checkIn)) {
                throw new IllegalArgumentException("La fecha de salida no puede ser anterior a la fecha de entrada");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Utilice YYYY-MM-DD", e);
        }
    }

    /**
     * Find persona by ID or throw exception
     */
    private Persona findPersonaById(Long personaId) {
        return personaRepository.findById(personaId)
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con ID: " + personaId));
    }

    /**
     * Find persona by email or throw exception
     */
//    private Persona findPersonaByEmail(String email) {
//        return personaRepository.findByEmail(email)
//                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada con email: " + email));
//    }

    /**
     * Find hotel by ID or throw exception
     */
    private Hotel findHotelById(Long hotelId) {
        return hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Hotel no encontrado con ID: " + hotelId));
    }

    /**
     * Find habitacion by ID or throw exception
     */
    private Habitaciones findHabitacionById(Long habitacionId) {
        return habitacionRepository.findById(habitacionId)
                .orElseThrow(() -> new EntityNotFoundException("Habitación no encontrada con ID: " + habitacionId));
    }
}