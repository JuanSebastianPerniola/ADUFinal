package com.example.demo.Reserva;

import com.example.demo.HabitacionesServiceJPA.IHabitacionJPA;
import com.example.demo.HotelServiceJpa.IHotelJPA;
import com.example.demo.PersonaServicio.PersonaRepository;
import com.example.demo.model.Habitaciones;
import com.example.demo.model.Persona;
import com.example.demo.model.Reserva;
import jakarta.persistence.EntityNotFoundException;
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
        // Opcional: verificar si existe primero
        reservaRepository.findById(id).ifPresent(reservaRepository::delete);

        // O simplemente
        reservaRepository.deleteById(id);
    }

    // Nuevo método para procesar y guardar reserva desde un archivo
    @Transactional
    public Reserva actualizarNombrePersona(Long reservaId, Reserva reservaActualizada) {
        // Fetch the existing reservation
        Reserva reservaExistente = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new EntityNotFoundException("Reserva not found with id: " + reservaId));

        // Just update the person's name if a person exists
        if (reservaActualizada.getPersona() != null && reservaActualizada.getPersona().getNombre() != null) {
            // Get the existing person to update
            Persona personaExistente = reservaExistente.getPersona();

            if (personaExistente != null) {
                // Update just the name
                personaExistente.setNombre(reservaActualizada.getPersona().getNombre());
                // If you want to update other person fields, add them here

                // Save the updated person
                personaRepository.save(personaExistente);
            }
        }

        // Return the updated reservation
        return reservaExistente;
    }
}
