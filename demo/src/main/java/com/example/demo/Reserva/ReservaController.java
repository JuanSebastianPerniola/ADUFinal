package com.example.demo.Reserva;

import com.example.demo.model.Reserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reserva")
public class ReservaController {
    @Autowired
    private ReservaService reservaService;

    @PostMapping("/reservar")
    public Reserva GuardarProducio(Reserva reserva){
        // aqui guardaremos la reserva con el nombre de la persona y el chekin, checkout,
        // date
        return reservaService.GuardarProducio(reserva);
    }

    // Mostrar todas las reservas hechas
    @PostMapping("/reservationList")
    public List<Reserva> MostrarReservas(Reserva reserva){
        return reservaService.ListarProducio(reserva);
    }

}
