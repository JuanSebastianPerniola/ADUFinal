package com.example.demo.NewData;

import com.example.demo.HotelService.PersonaRepository;
import com.example.demo.Reserva.IReservaJPA;
import com.example.demo.model.Persona;
import com.example.demo.model.Reserva;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LectorDatosMongo {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private IReservaJPA reservaRepository;

    public void procesarDatos(JsonNode jsonData) {
        JsonNode reservas = jsonData.get("reservas");
        if (reservas == null || !reservas.has("persona")) {
            return;
        }

        JsonNode personaNode = reservas.get("persona");

        String email = personaNode.get("email").asText();
        Persona persona = personaRepository.findByEmail(email).orElse(new Persona());

        persona.setNombre(personaNode.get("nombre").asText());
        persona.setApellido(personaNode.get("apellido").asText());
        persona.setEmail(email);
        persona.setEdad(personaNode.get("edad").asInt());

        persona = personaRepository.save(persona); // Guarda en MongoDB

        Reserva reserva = new Reserva();
        reserva.setCheckIn(personaNode.get("checkIn").asText());
        reserva.setCheckOut(personaNode.get("checkOut").asText());

        reservaRepository.save(reserva); // Guarda la reserva en MongoDB
    }
}
