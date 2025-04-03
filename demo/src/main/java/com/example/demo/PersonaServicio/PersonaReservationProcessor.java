package com.example.demo.PersonaServicio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.PersonaServicio.PersonaRepository;
import com.example.demo.model.Persona;
import com.example.demo.model.Reserva;
import com.example.demo.Reserva.IReservaJPA;

@Component
public class PersonaReservationProcessor {

    private static final Logger logger = LoggerFactory.getLogger(PersonaReservationProcessor.class);

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private IReservaJPA reservaRepository;

    /**
     * Creates a new Persona record for each reservation and associates it with the reservation
     * @param originalPersona The original persona from the reservation file
     * @param reserva The reservation to associate with the new persona
     * @return The updated reservation with the new persona
     */
    public Reserva createNewPersonaForReservation(Persona originalPersona, Reserva reserva) {
        try {
            // Create a new persona instance with copied data
            Persona newPersona = new Persona();
            newPersona.setNombre(originalPersona.getNombre());
            newPersona.setEmail(originalPersona.getEmail());

            if (originalPersona.getTelefono() != null) {
                newPersona.setTelefono(originalPersona.getTelefono());
            }

            // Add a unique identifier or reference to the original person
            newPersona.setReferenceId(originalPersona.getId().toString());

            // Save the new persona
            newPersona = personaRepository.save(newPersona);
            logger.info("Created new persona with ID: {} for reservation", newPersona.getId());

            // Update the reservation with the new persona
            reserva.setPersona(newPersona);

            return reserva;
        } catch (Exception e) {
            logger.error("Error creating new persona for reservation: {}", e.getMessage(), e);
            throw e;
        }
    }
}