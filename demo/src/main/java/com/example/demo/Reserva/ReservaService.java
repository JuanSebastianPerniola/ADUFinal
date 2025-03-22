package com.example.demo.Reserva;


import java.util.List;

import com.example.demo.HotelService.IHotelJPA;
import com.example.demo.model.Hotel;
import com.example.demo.model.Reserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Service
public class ReservaService {
    @Autowired
    private IReservaJPA ireservaJPA;

    // Proceso de reserva
    public Reserva GuardarProducio(Reserva reserva){
        return ireservaJPA.save(reserva);
    }

    /*
     * Listamos todas las opciones disponibles
     */
    public List<Reserva> ListarProducio(Reserva reserva){
        return ireservaJPA.findAll();
    }
}
