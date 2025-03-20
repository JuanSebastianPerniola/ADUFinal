package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long IdReserva;

    // mirar de que aqui ya venga la opcion selecionnada con el tipo de habitacion
    @ManyToMany
    @JoinColumn(name = "id_hotel")
    private long id_hotel;

    private Date checkOut, checkIn;

}
