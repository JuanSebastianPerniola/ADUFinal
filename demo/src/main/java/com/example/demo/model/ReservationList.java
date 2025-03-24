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
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "reservation_list_id") // This will create a foreign key in Reserva table
    private List<Reserva> reservas;
}
