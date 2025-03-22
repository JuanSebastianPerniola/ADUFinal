package com.example.demo.Reserva;

import com.example.demo.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IReservaJPA extends JpaRepository<Reserva,Integer> { }