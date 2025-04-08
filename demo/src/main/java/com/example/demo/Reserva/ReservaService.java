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
    @Transactional
    public Reserva save(Reserva reserva) {
        return reservaRepository.save(reserva);
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
}