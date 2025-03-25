package com.example.demo.Reserva;

import com.example.demo.model.Reserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/reserva")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    // Guardar una reserva
    @PostMapping("/reservar")
    public Reserva guardarReserva(@RequestBody Reserva reserva) {
        // Aquí guardamos la reserva con los detalles necesarios.
        return reservaService.GuardarReserva(reserva);
    }

    // Mostrar todas las reservas hechas
    @GetMapping("/listar")
    public List<Object[]> listarReservas() {
        return reservaService.listarReservas();
    }
}
