package com.example.demo.Reserva;

import com.example.demo.model.Reserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/reserva")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    // Guardar una reserva
    @PostMapping("/reservar")
    public Reserva guardarReserva(@RequestBody Reserva reserva) {
        // Generar codigo de reserva random
        // Aquí guardamos la reserva con los detalles necesarios.
        return reservaService.GuardarReserva(reserva);
    }

    // Mostrar todas las reservas hechas
    @GetMapping("/listar")
    public ResponseEntity<List<Object[]>> listarReservas() {
        List<Object[]> reservas = reservaService.listarReservas();
        return ResponseEntity.ok().body(reservas);
    }
}
