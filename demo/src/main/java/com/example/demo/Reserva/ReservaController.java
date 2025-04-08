package com.example.demo.Reserva;

import com.example.demo.PersonaServicio.*;
import com.example.demo.model.Persona;
import com.example.demo.model.Reserva;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    @PutMapping("/{reservaId}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> actualizarFechasReserva(
            @PathVariable Long reservaId,
            @RequestBody Map<String, String> payload) {

        Reserva reserva = reservaService.obtenerReservaPorId(reservaId);
        if (reserva == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reserva no encontrada.");
        }

        // Aquí puedes actualizar las fechas usando el payload
        String checkIn = payload.get("checkIn");
        String checkOut = payload.get("checkOut");
        // Actualizar las fechas en la reserva
        reserva.setCheckIn(LocalDate.parse(checkIn));
        reserva.setCheckOut(LocalDate.parse(checkOut));

        reservaService.save(reserva);

        return ResponseEntity.ok("Fechas actualizadas correctamente.");
    }

}