package com.example.demo.Reserva;

import com.example.demo.model.Reserva;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.nio.file.Paths;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/reserva")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Reserva>> listarReservas() {
        List<Reserva> reservas = reservaService.listarReservasCompletas();
        System.out.println("Reservas enviadas: " + reservas);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtenerReserva(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.obtenerReservaPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Long id) {
        reservaService.eliminarReserva(id);
        return ResponseEntity.noContent().build();
    }
}