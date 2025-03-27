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
    // Llamamos el IJPA de la parte de springbooty 🍑
    @Autowired
    private ReservaService reservaService;

    // Mostrar todas las reservas hechas
    @GetMapping("/listar")
    public ResponseEntity<List<Object[]>> listarReservas() {
        List<Object[]> reservas = reservaService.listarReservas();
        return ResponseEntity.ok().body(reservas);
    }

    // Delete
    @DeleteMapping("/deleteReservar/{id}")
    public ResponseEntity<String> elimnar(@PathVariable Long id) {
        // Elimanar reserva por id
        reservaService.EliminarReserva(id);
        return ResponseEntity.ok("Reserva eliminado correctamente");
    }

    // Upadte
    @PutMapping("/actualizar")
    public ResponseEntity<Reserva> Actualizar(@RequestBody Reserva reserva) {
        // Actualizar reserva
        Reserva reservaUpdate = reservaService.GurdarReserva(reserva);
        if(ResponseEntity.ok(reservaUpdate).hasBody()){
            return ResponseEntity.ok(reservaUpdate);
        }
        return (ResponseEntity<Reserva>) ResponseEntity.badRequest();
    }
}
