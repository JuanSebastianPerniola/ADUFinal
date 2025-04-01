package com.example.demo.HabitacionesServiceJPA;

import com.example.demo.model.Habitaciones;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IHabitacionJPA extends JpaRepository<Habitaciones, Long> {
}