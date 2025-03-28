package com.example.demo.Reserva;


import java.util.List;

import com.example.demo.HotelService.PersonaRepository;
import com.example.demo.model.Persona;
import java.util.Optional;
import com.example.demo.model.Reserva;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ReservaService {
    private IReservaJPA ireservaJPA;
    private PersonaRepository personaRepository;
    @Autowired
    public ReservaService(IReservaJPA ireservaJPA, PersonaRepository personaRepository) {
        this.ireservaJPA = ireservaJPA;
        this.personaRepository = personaRepository;
    }
    @Transactional
    public List<Object[]> listarReservas() {
        return ireservaJPA.findAllWithPersonas();
    }

    public void EliminarReserva(Long id) {
        ireservaJPA.deleteById(id);
    }

    public Reserva GuardarReserva(Reserva reserva) {
        return ireservaJPA.save(reserva);
    }

    public Reserva ActualizarReserva(Reserva reserva) {
        return ireservaJPA.findById(reserva.getIdReserva())
                .map(existingReserva -> {
                    existingReserva.setCheckIn(reserva.getCheckIn());
                    existingReserva.setCheckOut(reserva.getCheckOut());

                    // Verificar si la persona ya existe en la BD
                    Optional<Persona> personaExistente = personaRepository.findByEmail(reserva.getPersona().getEmail());

                    if (personaExistente.isPresent()) {
                        // Si existe la persona buscada (buscamos por id)
                        // se actualiza other wise
                        // devolvemos error
                        Persona personaActualizada = personaExistente.get();
                        personaActualizada.setNombre(reserva.getPersona().getNombre());
                        personaActualizada.setApellido(reserva.getPersona().getApellido());
                        personaActualizada.setEdad(reserva.getPersona().getEdad());
                        personaRepository.save(personaActualizada);
                        existingReserva.setPersona(personaActualizada);
                    } else {
                        // esto es inncesario pero por ahora lo dejaremos
                        existingReserva.setPersona(reserva.getPersona()); // Guardar nueva persona
                    }

                    return ireservaJPA.save(existingReserva);
                }).orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    }
}

