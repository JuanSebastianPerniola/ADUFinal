package com.example.demo.Reserva;

import com.example.demo.PersonaServicio.*;
import com.example.demo.model.Persona;
import com.example.demo.model.Reserva;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/reserva")
public class ReservaController {

    private final ReservaService reservaService;
    private final PersonaRepository personaRepository;

    public ReservaController(ReservaService reservaService, PersonaRepository personaRepository) {
        this.reservaService = reservaService;
        this.personaRepository = personaRepository;
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Reserva>> listarReservas() {
        List<Reserva> reservas = reservaService.listarReservasCompletas();
        System.out.println("Reservas enviadas: " + reservas);
        return ResponseEntity.ok(reservas);
    }
 // comentario de prueba

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Long id) {
        reservaService.eliminarReserva(id);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/persona/{personaId}")
    public ResponseEntity<Persona> actualizarNombrePersona(
            @PathVariable Long personaId,
            @RequestBody Persona persona) {

        Persona personaExistente = personaRepository.findById(personaId)
                .orElseThrow(() -> new EntityNotFoundException("Persona not found with id: " + personaId));

        personaExistente.setNombre(persona.getNombre());

        return ResponseEntity.ok(personaRepository.save(personaExistente));
    }
}