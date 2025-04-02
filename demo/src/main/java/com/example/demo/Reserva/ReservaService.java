package com.example.demo.Reserva;

import com.example.demo.HabitacionesServiceJPA.IHabitacionJPA;
import com.example.demo.HotelServiceJpa.IHotelJPA;
import com.example.demo.PersonaServicio.PersonaRepository;
import com.example.demo.model.Habitaciones;
import com.example.demo.model.Reserva;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservaService {

    private final IReservaJPA reservaRepository;
    private final IHabitacionJPA iHabitacionJPA;
    private final IHotelJPA iHotelJPA;
    private final PersonaRepository personaRepository;
    public ReservaService(IReservaJPA reservaRepository, IHabitacionJPA iHabitacionJPA, IHotelJPA iHotelJPA, PersonaRepository personaRepository) {
        this.reservaRepository = reservaRepository;
        this.iHabitacionJPA = iHabitacionJPA;
        this.iHotelJPA = iHotelJPA;
        this.personaRepository = personaRepository;
    }

    // read only para optimizar el hibernate
    // prevee modificacion accidentales
    @Transactional(readOnly = true)
    public List<Reserva> listarReservasCompletas() {
        return reservaRepository.findAllWithRelations();
    }

    @Transactional(readOnly = true)
    public Reserva obtenerReservaPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    }

    @Transactional
    public void eliminarReserva(Long id) {
        reservaRepository.deleteById(id);
    }

    // Nuevo método para procesar y guardar reserva desde un archivo
    @Transactional
    public Reserva guardarReserva(Long id, Reserva reserva) {
        // Cargar habitación existente con su hotel
        if (reserva.getHabitacion() != null && reserva.getHabitacion().getId() != null) {
            Habitaciones habitacionExistente = iHabitacionJPA.findById(reserva.getHabitacion().getId())
                    .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));
            reserva.setHabitacion(habitacionExistente);
        }
        if (reserva.getHotel() != null && reserva.getHotel().getId() == null) {
            iHotelJPA.save(reserva.getHotel());
        }

        if (reserva.getPersona() != null && reserva.getPersona().getId() == null) {
            personaRepository.save(reserva.getPersona());
        }

        // Luego guardar la reserva
        return reservaRepository.save(reserva);
    }
}
