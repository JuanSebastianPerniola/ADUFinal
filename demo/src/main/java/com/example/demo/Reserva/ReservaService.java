package com.example.demo.Reserva;

import com.example.demo.HabitacionesServiceJPA.IHabitacionJPA;
import com.example.demo.HotelServiceJpa.IHotelJPA;
import com.example.demo.PersonaServicio.PersonaRepository;
import com.example.demo.model.Habitaciones;
import com.example.demo.model.Hotel;
import com.example.demo.model.Persona;
import com.example.demo.model.Reserva;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReservaService {

    private final IReservaJPA reservaRepository;
    private final IHabitacionJPA iHabitacionJPA;
    private final IHotelJPA iHotelJPA;
    private final PersonaRepository personaRepository;

    public ReservaService(IReservaJPA reservaRepository,
                          IHabitacionJPA iHabitacionJPA,
                          IHotelJPA iHotelJPA,
                          PersonaRepository personaRepository) {
        this.reservaRepository = reservaRepository;
        this.iHabitacionJPA = iHabitacionJPA;
        this.iHotelJPA = iHotelJPA;
        this.personaRepository = personaRepository;
    }

    @Transactional(readOnly = true)
    public List<Reserva> listarReservasCompletas() {
        return reservaRepository.findAllWithRelations();
    }

    @Transactional(readOnly = true)
    public Reserva obtenerReservaPorId(Long id) {
        return reservaRepository.findByIdWithRelations(id)  // or findByIdWithRelations(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada"));
    }

    @Transactional
    public void eliminarReserva(Long id) {
        reservaRepository.deleteById(id);
    }

    @Transactional
    private Reserva parseJsonReservation(File jsonFile) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(jsonFile);

        // Validate required fields
        if (!root.has("personaId") || !root.has("checkIn") || !root.has("hotelId") || !root.has("habitacionId")) {
            throw new IllegalArgumentException("JSON debe contener personaId, hotelId, habitacionId y fechas");
        }

        // Load entities and ensure they're fully initialized
        Long personaId = root.get("personaId").asLong();
        Long hotelId = root.get("hotelId").asLong();
        Long habitacionId = root.get("habitacionId").asLong();

        Persona persona = personaRepository.getReferenceById(personaId);
        // Force initialization if needed
        persona.getId(); // This can force initialization of the proxy

        Hotel hotel = iHotelJPA.getReferenceById(hotelId);

        Habitaciones habitacion = iHabitacionJPA.getReferenceById(habitacionId);
        habitacion.getId(); // Force initialization

        // Create and populate reservation
        Reserva reserva = new Reserva();
        reserva.setPersona(persona);
        reserva.setHotel(hotel);
        reserva.setHabitacion(habitacion);
        reserva.setCheckIn(LocalDate.parse(root.get("checkIn").asText()));
        reserva.setCheckOut(LocalDate.parse(root.get("checkOut").asText()));

        return reserva;
    }

    @Transactional
    public Reserva actualizarNombrePersona(Long reservaId, String nuevoNombre) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada"));

        Persona persona = reserva.getPersona();
        personaRepository.save(persona);

        return reserva;
    }
}