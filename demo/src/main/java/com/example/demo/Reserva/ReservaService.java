package com.example.demo.Reserva;


import java.util.List;

import com.example.demo.HotelService.IHotelJPA;
import com.example.demo.model.Hotel;
import com.example.demo.model.Reserva;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Service
public class ReservaService {
    @Autowired
    private IReservaJPA ireservaJPA;
    // Listar todas las reservas con la información de la persona asociada
    @Transactional // Esto mantiene la sesión de Hibernate abierta hasta que termine la consulta
    public List<Object[]> listarReservas() {
        return ireservaJPA.findAllWithPersonas();
    }
    public void EliminarReserva(Long id){
        ireservaJPA.deleteById(id);
    }
    // Proceso de reserva no me sirve lo dejare para mantener
    public Reserva Actualizar(Reserva reserva){
        return ireservaJPA.save(reserva);
    }
    public Reserva GurdarReserva(Reserva reserva){
        return ireservaJPA.save(reserva);
    }
}
