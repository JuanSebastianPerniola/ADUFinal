package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idReserva;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkIn;

    @Setter
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOut;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotel_id", nullable = false)
    @JsonIncludeProperties({"idHotel", "nombre", "direccion"})
    private Hotel hotel;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "habitacion_id")
    private Habitaciones habitacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id", nullable = false)
    @JsonIncludeProperties({"id", "nombre", "apellidos", "email", "telefono"})
    private Persona persona;

    // Getters
    public Long getIdReserva() {
        return this.idReserva;
    }

    public LocalDate getCheckIn() {
        return this.checkIn;
    }

    public LocalDate getCheckOut() {
        return this.checkOut;
    }

    public Hotel getHotel() {
        return this.hotel;
    }

    public Habitaciones getHabitacion() {
        return this.habitacion;
    }

    public Persona getPersona() {
        return this.persona;
    }

    // Setters
    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public void setHabitacion(Habitaciones habitacion) {
        this.habitacion = habitacion;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }
}
