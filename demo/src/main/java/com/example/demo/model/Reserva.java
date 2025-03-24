package com.example.demo.model;

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

    // Una generada la reserva haremos que lo busque por el id de la reserva
    // este id se mostrara en pantalla y se enviara a un mail
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idReserva;

    // Relacion Muchos a Muchos con Hotel para
    // mostrar el hotel que se este haciendo la reserva
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
    
    @Column(nullable = false)
    private Date checkOut, checkIn;

    // Relacion con ReservationList
    @ManyToOne
    @JoinColumn(name = "reservation_list_id")
    private ReservationList reservationList;

    // Relacion con la tabla personas
    @ManyToOne
    @JoinColumn(name = "persona_id", nullable = false)
    private Persona persona; // Relación con Persona
}
