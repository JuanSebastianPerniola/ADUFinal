package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idReserva;

    @Column(nullable = false)
    private LocalDate checkOut, checkIn;

    // Relación con Hotel (Muchos a Uno)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita errores de serialización
    private Hotel hotel;

    // Relación con Persona (Muchos a Uno)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Persona persona;

    // Evita recursión infinita
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserva_padre_id")
    private Reserva reservaPadre;

    @JsonIgnore
    @OneToMany(mappedBy = "reservaPadre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserva> subReservas;


    @ManyToOne
    @JoinColumn(name = "habitacion_id", nullable = false)
    private Habitaciones habitacion; // Este nombre DEBE coincidir con mappedBy en Habitaciones

    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }
    // Getters y Setters adicionales
    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public List<Reserva> getSubReservas() {
        return subReservas;
    }

    public void setSubReservas(List<Reserva> subReservas) {
        this.subReservas = subReservas;
    }

    public void setTipoHabitacion(Habitaciones habitacion) {
    }
}
