package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReserva;

    @Column(nullable = false)
    private Date checkOut, checkIn;

    // Relación con Hotel (Muchos a Uno)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    // Relación con Persona (Muchos a Uno)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id", nullable = false)
    private Persona persona;

    // Evita recursión infinita
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserva_padre_id")
    private Reserva reservaPadre;

    @JsonIgnore
    @OneToMany(mappedBy = "reservaPadre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserva> subReservas;
}
