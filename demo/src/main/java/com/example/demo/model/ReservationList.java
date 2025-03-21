package com.example.demo.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ReservationList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    // con este join estamos cogiendo todos los siguientes valores =>
    // checkin, checkout,
    @JoinColumn(name = "reserva")
    private List<Reserva> reserva;
}
