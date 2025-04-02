package com.example.demo.Reserva;

import com.example.demo.model.Reserva;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservaService {

    private final IReservaJPA reservaRepository;

    public ReservaService(IReservaJPA reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    // read only para optimizar el hibernate
    // prevee modificacion accidentales
    @Transactional(readOnly = true)
    public List<Reserva> listarReservasCompletas() {
        return reservaRepository.findAllWithRelations();
    }

    @Transactional(readOnly = true)
    public Reserva obtenerReservaPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    }

    @Transactional
    public void eliminarReserva(Long id) {
        reservaRepository.deleteById(id);
    }

    // Nuevo método para procesar y guardar reserva desde un archivo
    @Transactional
    public Reserva guardarReserva(Reserva reserva) {
        return reservaRepository.save(reserva);
    }
}
