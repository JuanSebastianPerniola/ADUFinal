package com.example.demo.HabitacionesServiceJPA;

import com.example.demo.model.Habitaciones;
import com.example.demo.HabitacionesServiceJPA.IHabitacionJPA;
import org.springframework.stereotype.Service;

@Service
public class HabitacionesService {

    private final IHabitacionJPA repository;

    public HabitacionesService(IHabitacionJPA repository) {
        this.repository = repository;
    }

    // Métodos de servicio...
}