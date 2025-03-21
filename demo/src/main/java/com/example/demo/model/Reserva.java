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

    // Relación Muchos a Muchos con Hotel para
    // mostrar el hotel que se este haciendo la reserva
    @ManyToMany
    @JoinTable(
            name = "reserva_hotel", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "reserva_id"), // Clave foránea de Reserva
            inverseJoinColumns = @JoinColumn(name = "hotel_id") // Clave foránea de Hotel
    )
    private List<Hotel> hoteles; // Corrección: 'id_hotel' → 'hoteles'

    private Date checkOut, checkIn;

    // Relación con ReservationList
    @ManyToOne
    @JoinColumn(name = "reservation_list_id")
    private ReservationList reservationList;

    // Relación con persona
    @ManyToOne
    @JoinColumn(name = "persona_id")
    private List<Persona> personaList;
}
