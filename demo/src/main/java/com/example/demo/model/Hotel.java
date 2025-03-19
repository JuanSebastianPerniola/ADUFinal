package com.example.demo.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long ID;

    @ManyToMany
    @JoinColumn(name = "reserva")
    private Reserva reserva;

    @ManyToMany
    @JoinColumn(name = "habitaciones")
    private Reserva habitaciones;


}
